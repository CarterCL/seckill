package com.cl.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author CarterCL
 * @create 2020/10/8 21:26
 */
@Mapper
public interface UserMapper {

    List<Integer> listId(int limit, int offset);
}
