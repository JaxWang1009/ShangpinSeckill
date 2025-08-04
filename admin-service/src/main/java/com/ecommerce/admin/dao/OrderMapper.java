package com.ecommerce.admin.dao;

import com.ecommerce.admin.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 * 
 * @author ecommerce-team
 * @since 1.0.0
 */
@Mapper
public interface OrderMapper {
    
    /**
     * 分页查询订单列表
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 订单列表
     */
    List<Order> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 查询订单总数
     * 
     * @return 总数
     */
    Integer selectCount();
    
    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    Order selectById(@Param("id") Long id);
    
    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 插入订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    Integer insert(Order order);
    
    /**
     * 更新订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    Integer update(Order order);
    
    /**
     * 更新订单状态
     * 
     * @param id 订单ID
     * @param status 订单状态
     * @return 影响行数
     */
    Integer updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 