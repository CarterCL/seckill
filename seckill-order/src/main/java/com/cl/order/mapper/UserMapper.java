package com.cl.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author CarterCL
 * @create 2020/10/8 21:26
 */
@Repository
@Mapper
public interface UserMapper {

    List<Integer> listId(int limit, int offset);
}
