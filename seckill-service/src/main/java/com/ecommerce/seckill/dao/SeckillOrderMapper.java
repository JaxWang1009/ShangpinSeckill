package com.ecommerce.seckill.dao;

import com.ecommerce.seckill.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀订单Mapper接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Mapper
public interface SeckillOrderMapper {
    
    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    SeckillOrder selectById(@Param("id") Long id);
    
    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    SeckillOrder selectByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 根据用户ID查询订单列表
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    List<SeckillOrder> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 插入订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    Integer insert(SeckillOrder order);
    
    /**
     * 更新订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    Integer update(SeckillOrder order);
    
    /**
     * 删除订单
     * 
     * @param id 订单ID
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);
} 