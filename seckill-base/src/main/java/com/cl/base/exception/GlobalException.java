package com.cl.base.exception;

import com.cl.base.enums.IEnum;
import lombok.Getter;

/**
 * @author CarterCL
 * @create 2020/9/29 21:53
 */
@Getter
public class GlobalException extends RuntimeException {

    private IEnum resultEnum;

    public GlobalException(IEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }
}
