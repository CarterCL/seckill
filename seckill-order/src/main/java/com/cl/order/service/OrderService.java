package com.cl.order.service;

import com.cl.order.dto.SeckillDTO;
import com.cl.order.mapper.OrderMapper;
import com.cl.order.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CarterCL
 * @create 2020/10/4 21:12
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private static final Integer MAX_REDUCE_STOCK_RETRY = 5;

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(SeckillDTO seckillDTO){


    }
}
