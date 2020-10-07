package com.cl.base.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author CarterCL
 * @create 2020/9/29 21:08
 */
@Getter
@Setter
public class SeckillDTO {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 0, message = "用户ID不能为负数")
    private Integer userId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 0, message = "商品ID不能为负数")
    private Integer productId;
}
