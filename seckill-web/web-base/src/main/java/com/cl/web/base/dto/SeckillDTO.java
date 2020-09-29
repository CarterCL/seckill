package com.cl.web.base.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author CarterCL
 * @create 2020/9/29 21:08
 */
@Getter
@Setter
public class SeckillDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 0, message = "商品ID不能为负数")
    private Integer productId;
}
