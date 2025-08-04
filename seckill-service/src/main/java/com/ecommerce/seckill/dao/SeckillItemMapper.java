package com.ecommerce.seckill.dao;

import com.ecommerce.seckill.entity.SeckillItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀商品Mapper接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Mapper
public interface SeckillItemMapper {
    
    /**
     * 根据ID查询秒杀商品
     * 
     * @param id 商品ID
     * @return 秒杀商品信息
     */
    SeckillItem selectById(@Param("id") Long id);
    
    /**
     * 根据关键词搜索秒杀商品
     * 
     * @param keyword 关键词
     * @return 商品列表
     */
    List<SeckillItem> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 插入秒杀商品
     * 
     * @param item 秒杀商品信息
     * @return 影响行数
     */
    Integer insert(SeckillItem item);
    
    /**
     * 更新秒杀商品
     * 
     * @param item 秒杀商品信息
     * @return 影响行数
     */
    Integer update(SeckillItem item);
    
    /**
     * 删除秒杀商品
     * 
     * @param id 商品ID
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);
} 