package com.ecommerce.seckill.service.impl;

import cn.hutool.core.util.IdUtil;
import com.ecommerce.seckill.dao.SeckillItemMapper;
import com.ecommerce.seckill.dao.SeckillOrderMapper;
import com.ecommerce.seckill.entity.SeckillItem;
import com.ecommerce.seckill.entity.SeckillOrder;
import com.ecommerce.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 秒杀服务实现类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
    
    @Autowired
    private SeckillItemMapper seckillItemMapper;
    
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    
    @Autowired
    private ThreadPoolExecutor seckillThreadPool;
    
    /**
     * 线程池最大并发数
     */
    @Value("${seckill.thread-pool.max-concurrent:200}")
    private Integer maxConcurrent;
    
    /**
     * 订单超时时间（分钟）
     */
    @Value("${seckill.order.timeout:30}")
    private Integer orderTimeout;
    
    /**
     * Lua脚本执行器
     */
    private DefaultRedisScript<Long> stockScript;
    
    /**
     * 初始化Lua脚本
     */
    @PostConstruct
    public void init() {
        stockScript = new DefaultRedisScript<>();
        stockScript.setLocation(new ClassPathResource("lua/stock.lua"));
        stockScript.setResultType(Long.class);
        
        log.info("秒杀服务初始化完成，线程池最大并发数: {}, 订单超时时间: {}分钟", maxConcurrent, orderTimeout);
    }
    
    @Override
    public List<SeckillItem> searchItems(String keyword) {
        try {
            // 构建ES查询
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 多字段搜索：标题、描述
                queryBuilder.withQuery(org.springframework.data.elasticsearch.core.query.Query.matchQuery("title", keyword)
                    .or(org.springframework.data.elasticsearch.core.query.Query.matchQuery("description", keyword)));
            }
            
            // 只查询激活状态的商品
            queryBuilder.withFilter(org.springframework.data.elasticsearch.core.query.Query.termQuery("is_active", 1));
            
            // 按创建时间倒序
            queryBuilder.withSort(org.springframework.data.elasticsearch.core.query.Sort.by("create_time").descending());
            
            NativeSearchQuery searchQuery = queryBuilder.build();
            SearchHits<SeckillItem> searchHits = elasticsearchTemplate.search(searchQuery, SeckillItem.class);
            
            // 转换为List并检查库存状态
            return searchHits.getSearchHits().stream()
                .map(hit -> {
                    SeckillItem item = hit.getContent();
                    // 检查Redis中的库存状态，添加"售罄"标识
                    String stockKey = "seckill:stock:" + item.getId();
                    Object stock = redisTemplate.opsForValue().get(stockKey);
                    if (stock != null && Integer.parseInt(stock.toString()) <= 0) {
                        item.setTitle(item.getTitle() + " [售罄]");
                    }
                    return item;
                })
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("搜索秒杀商品失败: {}", e.getMessage(), e);
            // 降级到数据库查询
            return seckillItemMapper.searchByKeyword(keyword);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String executeSeckill(Long itemId, Long userId, Integer quantity) {
        // 使用线程池控制并发
        if (seckillThreadPool.getActiveCount() >= maxConcurrent) {
            throw new RuntimeException("系统繁忙，请稍后重试");
        }
        
        try {
            // 1. 查询商品信息
            SeckillItem item = seckillItemMapper.selectById(itemId);
            if (item == null) {
                throw new RuntimeException("商品不存在");
            }
            
            // 2. 检查商品是否激活
            if (item.getIsActive() != 1) {
                throw new RuntimeException("商品未激活");
            }
            
            // 3. 检查秒杀时间
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(item.getStartTime()) || now.isAfter(item.getEndTime())) {
                throw new RuntimeException("不在秒杀时间内");
            }
            
            // 4. 检查库存是否充足
            if (item.getStock() < quantity) {
                throw new RuntimeException("库存不足");
            }
            
            // 5. 执行Redis Lua脚本进行库存扣减和防重复下单
            String stockKey = "seckill:stock:" + itemId;
            String lockKey = "lock:seckill:" + itemId + ":" + userId;
            
            Long result = redisTemplate.execute(stockScript, 
                Arrays.asList(stockKey, lockKey), 
                userId.toString(), 
                quantity.toString());
            
            // 6. 根据Lua脚本返回结果处理
            if (result == null || result < 0) {
                String errorMsg = getErrorMessage(result);
                throw new RuntimeException(errorMsg);
            }
            
            // 7. 创建订单
            String orderNo = createOrder(itemId, userId, quantity, item.getSeckillPrice());
            
            // 8. 发送延迟消息处理订单超时
            sendOrderTimeoutMessage(orderNo);
            
            log.info("用户 {} 秒杀商品 {} 成功，订单号: {}, 数量: {}", userId, itemId, orderNo, quantity);
            return orderNo;
            
        } catch (Exception e) {
            log.error("秒杀失败 - 用户: {}, 商品: {}, 错误: {}", userId, itemId, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(String orderNo) {
        try {
            // 1. 查询订单
            SeckillOrder order = seckillOrderMapper.selectByOrderNo(orderNo);
            if (order == null) {
                throw new RuntimeException("订单不存在");
            }
            
            // 2. 检查订单状态
            if (order.getStatus() != 0) {
                throw new RuntimeException("订单状态不允许取消");
            }
            
            // 3. 更新订单状态为已取消
            order.setStatus(-1);
            order.setUpdateTime(LocalDateTime.now());
            seckillOrderMapper.update(order);
            
            // 4. 回滚库存
            String stockKey = "seckill:stock:" + order.getItemId();
            redisTemplate.opsForValue().increment(stockKey, order.getQuantity());
            
            log.info("订单 {} 取消成功，库存已回滚，商品ID: {}, 数量: {}", 
                orderNo, order.getItemId(), order.getQuantity());
            return true;
            
        } catch (Exception e) {
            log.error("取消订单失败 - 订单号: {}, 错误: {}", orderNo, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean handlePaymentCallback(String orderNo) {
        try {
            // 1. 查询订单
            SeckillOrder order = seckillOrderMapper.selectByOrderNo(orderNo);
            if (order == null) {
                throw new RuntimeException("订单不存在");
            }
            
            // 2. 检查订单状态
            if (order.getStatus() != 0) {
                log.warn("订单 {} 状态异常，当前状态: {}", orderNo, order.getStatus());
                return false;
            }
            
            // 3. 更新订单状态为已支付
            order.setStatus(1);
            order.setPayTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            seckillOrderMapper.update(order);
            
            // 4. 发送MQ消息同步更新ES商品状态
            sendPaymentSuccessMessage(orderNo, order.getItemId());
            
            log.info("订单 {} 支付成功，商品ID: {}", orderNo, order.getItemId());
            return true;
            
        } catch (Exception e) {
            log.error("处理支付回调失败 - 订单号: {}, 错误: {}", orderNo, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 创建订单
     */
    private String createOrder(Long itemId, Long userId, Integer quantity, BigDecimal price) {
        SeckillOrder order = new SeckillOrder();
        order.setOrderNo(IdUtil.fastSimpleUUID());
        order.setUserId(userId);
        order.setItemId(itemId);
        order.setQuantity(quantity);
        order.setAmount(price.multiply(new BigDecimal(quantity)));
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        seckillOrderMapper.insert(order);
        return order.getOrderNo();
    }
    
    /**
     * 发送订单超时消息
     */
    private void sendOrderTimeoutMessage(String orderNo) {
        try {
            // 发送延迟消息，30分钟后处理订单超时
            rocketMQTemplate.syncSend("ORDER_TIMEOUT_TOPIC", orderNo, orderTimeout * 60 * 1000);
            log.debug("发送订单超时消息成功，订单号: {}, 延迟时间: {}分钟", orderNo, orderTimeout);
        } catch (Exception e) {
            log.error("发送订单超时消息失败，订单号: {}, 错误: {}", orderNo, e.getMessage(), e);
        }
    }
    
    /**
     * 发送支付成功消息
     */
    private void sendPaymentSuccessMessage(String orderNo, Long itemId) {
        try {
            // 发送支付成功消息，用于同步更新ES
            rocketMQTemplate.syncSend("PAYMENT_SUCCESS_TOPIC", orderNo);
            log.debug("发送支付成功消息成功，订单号: {}, 商品ID: {}", orderNo, itemId);
        } catch (Exception e) {
            log.error("发送支付成功消息失败，订单号: {}, 错误: {}", orderNo, e.getMessage(), e);
        }
    }
    
    /**
     * 获取错误消息
     */
    private String getErrorMessage(Long result) {
        if (result == null) {
            return "系统错误";
        }
        switch (result.intValue()) {
            case -1:
                return "商品不存在";
            case -2:
                return "库存不足";
            case -3:
                return "重复下单，请勿重复操作";
            default:
                return "未知错误";
        }
    }
} 