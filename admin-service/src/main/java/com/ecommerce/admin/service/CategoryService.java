package com.ecommerce.admin.service;

import com.ecommerce.admin.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
public interface CategoryService {
    
    /**
     * 分页查询分类列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 分类列表
     */
    List<Category> getCategoryPage(Integer page, Integer size);
    
    /**
     * 根据ID查询分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    Category getCategoryById(Long id);
    
    /**
     * 创建分类
     * 
     * @param category 分类信息
     * @return 是否成功
     */
    Boolean createCategory(Category category);
    
    /**
     * 更新分类
     * 
     * @param category 分类信息
     * @return 是否成功
     */
    Boolean updateCategory(Category category);
    
    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 是否成功
     */
    Boolean deleteCategory(Long id);
    
    /**
     * 获取分类树形结构
     * 
     * @return 分类树
     */
    List<Category> getCategoryTree();
} 