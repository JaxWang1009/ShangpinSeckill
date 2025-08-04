package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dao.CategoryMapper;
import com.ecommerce.admin.entity.Category;
import com.ecommerce.admin.service.CategoryService;
import com.ecommerce.common.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类服务实现类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public List<Category> getCategoryPage(Integer page, Integer size) {
        // 计算偏移量
        Integer offset = (page - 1) * size;
        // 查询分页数据
        return categoryMapper.selectPage(offset, size);
    }
    
    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createCategory(Category category) {
        // 设置创建时间和创建人
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateBy(UserContext.getCurrentUsername());
        category.setUpdateBy(UserContext.getCurrentUsername());
        
        // 插入分类
        Integer result = categoryMapper.insert(category);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategory(Category category) {
        // 设置更新时间
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateBy(UserContext.getCurrentUsername());
        
        // 更新分类
        Integer result = categoryMapper.update(category);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCategory(Long id) {
        // 删除分类
        Integer result = categoryMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public List<Category> getCategoryTree() {
        // 查询所有分类
        return categoryMapper.selectAll();
    }
} 