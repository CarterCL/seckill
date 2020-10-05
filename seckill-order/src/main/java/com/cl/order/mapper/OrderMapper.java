package com.cl.order.mapper;


import com.cl.base.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CarterCL
 * @create 2020/10/4 19:46
 */
@Mapper
public interface OrderMapper {
    void insert(Order order);
}
