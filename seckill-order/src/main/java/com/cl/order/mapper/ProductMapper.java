package com.cl.order.mapper;

import com.cl.base.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author CarterCL
 * @create 2020/10/4 19:54
 */
@Repository
@Mapper
public interface ProductMapper {

    Integer findVersionById(Integer id);

    int updateStock(Integer id, Integer version, Integer stock);

    List<Product> listProductStock();

    void initStock(Integer id, Integer stock);
}
