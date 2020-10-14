package com.cl.base.exception;

import lombok.Getter;

/**
 * @author CarterCL
 * @create 2020/9/29 21:53
 */
@Getter
public class GlobalException extends RuntimeException {

    private final String message;

    public GlobalException(String message) {
        this.message = message;
    }
}
