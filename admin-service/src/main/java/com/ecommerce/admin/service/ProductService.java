package com.ecommerce.admin.service;

import com.ecommerce.admin.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
public interface ProductService {
    
    /**
     * 分页查询商品列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param categoryId 分类ID（可选）
     * @return 商品列表
     */
    List<Product> getProductPage(Integer page, Integer size, Long categoryId);
    
    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    Product getProductById(Long id);
    
    /**
     * 创建商品
     * 
     * @param product 商品信息
     * @return 是否成功
     */
    Boolean createProduct(Product product);
    
    /**
     * 更新商品
     * 
     * @param product 商品信息
     * @return 是否成功
     */
    Boolean updateProduct(Product product);
    
    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 是否成功
     */
    Boolean deleteProduct(Long id);
    
    /**
     * 调整商品库存
     * 
     * @param id 商品ID
     * @param stock 库存数量
     * @return 是否成功
     */
    Boolean adjustStock(Long id, Integer stock);
} 