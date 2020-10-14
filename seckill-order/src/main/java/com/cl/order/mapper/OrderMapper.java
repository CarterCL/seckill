package com.cl.order.mapper;


import com.cl.base.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author CarterCL
 * @create 2020/10/4 19:46
 */
@Repository
@Mapper
public interface OrderMapper {
    void insert(Order order);
}
