package com.cl.base.enums;

import lombok.Getter;

/**
 * @author CarterCL
 * @create 2020/10/4 21:36
 */
@Getter
public enum OrderStatusEnum {

    SECKILL_SUCCESS(1, "秒杀成功"),
    ABNORMAL(3, "订单异常");

    private final Integer code;
    private final String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
