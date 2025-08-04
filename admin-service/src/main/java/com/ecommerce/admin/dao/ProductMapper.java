package com.ecommerce.admin.dao;

import com.ecommerce.admin.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品Mapper接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Mapper
public interface ProductMapper {
    
    /**
     * 分页查询商品列表
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @param categoryId 分类ID（可选）
     * @return 商品列表
     */
    List<Product> selectPage(@Param("offset") Integer offset, 
                           @Param("limit") Integer limit, 
                           @Param("categoryId") Long categoryId);
    
    /**
     * 查询商品总数
     * 
     * @param categoryId 分类ID（可选）
     * @return 总数
     */
    Integer selectCount(@Param("categoryId") Long categoryId);
    
    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    Product selectById(@Param("id") Long id);
    
    /**
     * 插入商品
     * 
     * @param product 商品信息
     * @return 影响行数
     */
    Integer insert(Product product);
    
    /**
     * 更新商品
     * 
     * @param product 商品信息
     * @return 影响行数
     */
    Integer update(Product product);
    
    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);
    
    /**
     * 更新商品库存
     * 
     * @param id 商品ID
     * @param stock 库存数量
     * @return 影响行数
     */
    Integer updateStock(@Param("id") Long id, @Param("stock") Integer stock);
} 