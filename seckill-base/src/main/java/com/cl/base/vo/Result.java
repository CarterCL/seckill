package com.cl.base.vo;


import com.cl.base.enums.IEnum;
import lombok.Getter;

/**
 * @author CarterCL
 * @create 2020/9/29 21:57
 */
@Getter
public class Result<T> {

    private String code;
    private String message;
    private T data;

    public Result<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> Result<T> makeResult(IEnum iEnum, T data) {
        return new Result<T>().setCode(iEnum.getCode()).setMessage(iEnum.getMessage()).setData(data);
    }

    public static <T> Result<T> makeResult(IEnum iEnum) {
        return new Result<T>().setCode(iEnum.getCode()).setMessage(iEnum.getMessage()).setData(null);
    }

}
