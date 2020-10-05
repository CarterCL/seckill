package com.cl.base.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author CarterCL
 * @create 2020/9/29 20:45
 */
@Getter
@Setter
public class Product {

    private Integer id;
    private String productNo;
    private String productName;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
