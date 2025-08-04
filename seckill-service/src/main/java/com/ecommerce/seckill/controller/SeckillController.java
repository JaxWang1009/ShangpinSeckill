package com.ecommerce.seckill.controller;

import com.ecommerce.common.result.Result;
import com.ecommerce.seckill.entity.SeckillItem;
import com.ecommerce.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀控制器
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    
    @Autowired
    private SeckillService seckillService;
    
    /**
     * 搜索秒杀商品
     * 
     * @param keyword 关键词
     * @return 商品列表
     */
    @GetMapping("/items")
    public Result<List<SeckillItem>> searchItems(@RequestParam(required = false) String keyword) {
        try {
            List<SeckillItem> items = seckillService.searchItems(keyword);
            return Result.success(items);
        } catch (Exception e) {
            log.error("搜索秒杀商品失败: {}", e.getMessage());
            return Result.error("搜索秒杀商品失败");
        }
    }
    
    /**
     * 执行秒杀
     * 
     * @param itemId 商品ID
     * @param userId 用户ID
     * @param quantity 购买数量
     * @return 订单号
     */
    @PostMapping("/{itemId}")
    public Result<String> executeSeckill(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            String orderNo = seckillService.executeSeckill(itemId, userId, quantity);
            return Result.success("秒杀成功", orderNo);
        } catch (Exception e) {
            log.error("秒杀失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 取消订单
     * 
     * @param orderNo 订单号
     * @return 操作结果
     */
    @DeleteMapping("/orders/{orderNo}")
    public Result<Boolean> cancelOrder(@PathVariable String orderNo) {
        try {
            Boolean result = seckillService.cancelOrder(orderNo);
            if (result) {
                return Result.success("取消订单成功", true);
            } else {
                return Result.error("取消订单失败");
            }
        } catch (Exception e) {
            log.error("取消订单失败: {}", e.getMessage());
            return Result.error("取消订单失败");
        }
    }
    
    /**
     * 支付回调
     * 
     * @param orderNo 订单号
     * @return 操作结果
     */
    @PostMapping("/payment/callback")
    public Result<Boolean> paymentCallback(@RequestParam String orderNo) {
        try {
            Boolean result = seckillService.handlePaymentCallback(orderNo);
            if (result) {
                return Result.success("支付回调处理成功", true);
            } else {
                return Result.error("支付回调处理失败");
            }
        } catch (Exception e) {
            log.error("支付回调处理失败: {}", e.getMessage());
            return Result.error("支付回调处理失败");
        }
    }
} 