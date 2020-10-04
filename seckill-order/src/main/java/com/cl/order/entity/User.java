package com.cl.order.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author CarterCL
 * @create 2020/9/29 20:48
 */
@Getter
@Setter
public class User {

    private Integer id;
    private String username;
    private String phoneNumber;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
