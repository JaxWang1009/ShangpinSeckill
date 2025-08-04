package com.ecommerce.admin.controller;

import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.service.ProductService;
import com.ecommerce.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 分页查询商品列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param categoryId 分类ID（可选）
     * @return 商品列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('goods:view')")
    public Result<List<Product>> getProductPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId) {
        try {
            List<Product> products = productService.getProductPage(page, size, categoryId);
            return Result.success(products);
        } catch (Exception e) {
            log.error("查询商品列表失败: {}", e.getMessage());
            return Result.error("查询商品列表失败");
        }
    }
    
    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('goods:view')")
    public Result<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return Result.error("商品不存在");
            }
            return Result.success(product);
        } catch (Exception e) {
            log.error("查询商品失败: {}", e.getMessage());
            return Result.error("查询商品失败");
        }
    }
    
    /**
     * 创建商品
     * 
     * @param product 商品信息
     * @return 操作结果
     */
    @PostMapping
    @PreAuthorize("hasAuthority('goods:manage')")
    public Result<Boolean> createProduct(@RequestBody Product product) {
        try {
            Boolean result = productService.createProduct(product);
            if (result) {
                return Result.success("创建商品成功", true);
            } else {
                return Result.error("创建商品失败");
            }
        } catch (Exception e) {
            log.error("创建商品失败: {}", e.getMessage());
            return Result.error("创建商品失败");
        }
    }
    
    /**
     * 更新商品
     * 
     * @param id 商品ID
     * @param product 商品信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('goods:manage')")
    public Result<Boolean> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            product.setId(id);
            Boolean result = productService.updateProduct(product);
            if (result) {
                return Result.success("更新商品成功", true);
            } else {
                return Result.error("更新商品失败");
            }
        } catch (Exception e) {
            log.error("更新商品失败: {}", e.getMessage());
            return Result.error("更新商品失败");
        }
    }
    
    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('goods:manage')")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        try {
            Boolean result = productService.deleteProduct(id);
            if (result) {
                return Result.success("删除商品成功", true);
            } else {
                return Result.error("删除商品失败");
            }
        } catch (Exception e) {
            log.error("删除商品失败: {}", e.getMessage());
            return Result.error("删除商品失败");
        }
    }
    
    /**
     * 调整商品库存
     * 
     * @param id 商品ID
     * @param stock 库存数量
     * @return 操作结果
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAuthority('goods:manage')")
    public Result<Boolean> adjustStock(@PathVariable Long id, @RequestParam Integer stock) {
        try {
            Boolean result = productService.adjustStock(id, stock);
            if (result) {
                return Result.success("调整库存成功", true);
            } else {
                return Result.error("调整库存失败");
            }
        } catch (Exception e) {
            log.error("调整库存失败: {}", e.getMessage());
            return Result.error("调整库存失败");
        }
    }
} 