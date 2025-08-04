package com.ecommerce.seckill.service;

import com.ecommerce.seckill.entity.SeckillItem;
import com.ecommerce.seckill.entity.SeckillOrder;

import java.util.List;

/**
 * 秒杀服务接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
public interface SeckillService {
    
    /**
     * 搜索秒杀商品
     * 
     * @param keyword 关键词
     * @return 商品列表
     */
    List<SeckillItem> searchItems(String keyword);
    
    /**
     * 执行秒杀
     * 
     * @param itemId 商品ID
     * @param userId 用户ID
     * @param quantity 购买数量
     * @return 订单号
     */
    String executeSeckill(Long itemId, Long userId, Integer quantity);
    
    /**
     * 取消订单
     * 
     * @param orderNo 订单号
     * @return 是否成功
     */
    Boolean cancelOrder(String orderNo);
    
    /**
     * 支付回调处理
     * 
     * @param orderNo 订单号
     * @return 是否成功
     */
    Boolean handlePaymentCallback(String orderNo);
} 