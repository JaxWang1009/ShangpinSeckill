package com.ecommerce.admin.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Data
public class Product {
    
    /**
     * 商品ID
     */
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 商品库存
     */
    private Integer stock;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 商品图片
     */
    private String image;
    
    /**
     * 商品状态（0-下架，1-上架）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 更新人
     */
    private String updateBy;
} 