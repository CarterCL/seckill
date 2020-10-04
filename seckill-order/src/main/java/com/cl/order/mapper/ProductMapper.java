package com.cl.order.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author CarterCL
 * @create 2020/10/4 19:54
 */
@Mapper
public interface ProductMapper {

    Integer findVersionById(Integer id);

    int updateStock(Integer id, Integer version);
}
