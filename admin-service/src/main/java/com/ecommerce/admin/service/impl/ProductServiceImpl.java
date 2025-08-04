package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dao.ProductMapper;
import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.service.ProductService;
import com.ecommerce.common.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品服务实现类
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Override
    public List<Product> getProductPage(Integer page, Integer size, Long categoryId) {
        // 计算偏移量
        Integer offset = (page - 1) * size;
        // 查询分页数据
        return productMapper.selectPage(offset, size, categoryId);
    }
    
    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createProduct(Product product) {
        // 设置创建时间和创建人
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        product.setCreateBy(UserContext.getCurrentUsername());
        product.setUpdateBy(UserContext.getCurrentUsername());
        
        // 插入商品
        Integer result = productMapper.insert(product);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProduct(Product product) {
        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());
        product.setUpdateBy(UserContext.getCurrentUsername());
        
        // 更新商品
        Integer result = productMapper.update(product);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProduct(Long id) {
        // 删除商品
        Integer result = productMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adjustStock(Long id, Integer stock) {
        // 记录操作日志
        log.info("用户 {} 调整商品 {} 库存为 {}", UserContext.getCurrentUsername(), id, stock);
        
        // 更新商品库存
        Integer result = productMapper.updateStock(id, stock);
        return result > 0;
    }
} 