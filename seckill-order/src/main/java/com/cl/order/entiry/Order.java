package com.cl.order.entiry;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author CarterCL
 * @create 2020/9/29 20:42
 */
@Getter
@Setter
public class Order {

    private Integer id;
    private String orderNo;
    private Integer userId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
