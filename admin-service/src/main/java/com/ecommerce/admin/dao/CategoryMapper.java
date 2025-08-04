package com.ecommerce.admin.dao;

import com.ecommerce.admin.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类Mapper接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper {
    
    /**
     * 分页查询分类列表
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 分类列表
     */
    List<Category> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 查询分类总数
     * 
     * @return 总数
     */
    Integer selectCount();
    
    /**
     * 根据ID查询分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    Category selectById(@Param("id") Long id);
    
    /**
     * 插入分类
     * 
     * @param category 分类信息
     * @return 影响行数
     */
    Integer insert(Category category);
    
    /**
     * 更新分类
     * 
     * @param category 分类信息
     * @return 影响行数
     */
    Integer update(Category category);
    
    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);
    
    /**
     * 查询所有分类（树形结构）
     * 
     * @return 分类列表
     */
    List<Category> selectAll();
} 