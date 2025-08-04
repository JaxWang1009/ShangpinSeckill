package com.ecommerce.admin.controller;

import com.ecommerce.admin.entity.Category;
import com.ecommerce.admin.service.CategoryService;
import com.ecommerce.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类控制器
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 分页查询分类列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 分类列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('category:view')")
    public Result<List<Category>> getCategoryPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            List<Category> categories = categoryService.getCategoryPage(page, size);
            return Result.success(categories);
        } catch (Exception e) {
            log.error("查询分类列表失败: {}", e.getMessage());
            return Result.error("查询分类列表失败");
        }
    }
    
    /**
     * 根据ID查询分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('category:view')")
    public Result<Category> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                return Result.error("分类不存在");
            }
            return Result.success(category);
        } catch (Exception e) {
            log.error("查询分类失败: {}", e.getMessage());
            return Result.error("查询分类失败");
        }
    }
    
    /**
     * 创建分类
     * 
     * @param category 分类信息
     * @return 操作结果
     */
    @PostMapping
    @PreAuthorize("hasAuthority('category:manage')")
    public Result<Boolean> createCategory(@RequestBody Category category) {
        try {
            Boolean result = categoryService.createCategory(category);
            if (result) {
                return Result.success("创建分类成功", true);
            } else {
                return Result.error("创建分类失败");
            }
        } catch (Exception e) {
            log.error("创建分类失败: {}", e.getMessage());
            return Result.error("创建分类失败");
        }
    }
    
    /**
     * 更新分类
     * 
     * @param id 分类ID
     * @param category 分类信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('category:manage')")
    public Result<Boolean> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            category.setId(id);
            Boolean result = categoryService.updateCategory(category);
            if (result) {
                return Result.success("更新分类成功", true);
            } else {
                return Result.error("更新分类失败");
            }
        } catch (Exception e) {
            log.error("更新分类失败: {}", e.getMessage());
            return Result.error("更新分类失败");
        }
    }
    
    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('category:manage')")
    public Result<Boolean> deleteCategory(@PathVariable Long id) {
        try {
            Boolean result = categoryService.deleteCategory(id);
            if (result) {
                return Result.success("删除分类成功", true);
            } else {
                return Result.error("删除分类失败");
            }
        } catch (Exception e) {
            log.error("删除分类失败: {}", e.getMessage());
            return Result.error("删除分类失败");
        }
    }
    
    /**
     * 获取分类树形结构
     * 
     * @return 分类树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('category:view')")
    public Result<List<Category>> getCategoryTree() {
        try {
            List<Category> categories = categoryService.getCategoryTree();
            return Result.success(categories);
        } catch (Exception e) {
            log.error("查询分类树失败: {}", e.getMessage());
            return Result.error("查询分类树失败");
        }
    }
} 