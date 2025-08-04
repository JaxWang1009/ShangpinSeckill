package com.ecommerce.seckill.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀商品实体类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Data
public class SeckillItem {
    
    /**
     * 秒杀商品ID
     */
    private Long id;
    
    /**
     * 商品标题
     */
    private String title;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    
    /**
     * 商品库存
     */
    private Integer stock;
    
    /**
     * 是否激活（0-未激活，1-已激活）
     */
    private Integer isActive;
    
    /**
     * 秒杀开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 秒杀结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 