package com.cl.order.service;


import com.cl.base.dto.SeckillDTO;
import com.cl.base.entity.Order;
import com.cl.base.enums.OrderStatusEnum;
import com.cl.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CarterCL
 * @create 2020/10/4 21:12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private final OrderMapper orderMapper;
    private final ProductService productService;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(SeckillDTO seckillDTO){

        Order order = new Order();
        order.setUserId(seckillDTO.getUserId());
        order.setProductId(seckillDTO.getProductId());
        order.setStatus(OrderStatusEnum.SECKILL_SUCCESS.getCode());

        // 订单入库
        orderMapper.insert(order);

        // 减库存
        productService.reduceStock(seckillDTO.getProductId());
    }
}
