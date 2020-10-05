package com.cl.web.spring.aspect;


import com.cl.base.enums.CodeEnum;
import com.cl.base.exception.GlobalException;
import com.cl.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author CarterCL
 * @create 2020/9/29 22:09
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public Result<Object> handleGlobalException(GlobalException ex) {

        return Result.makeResult(ex.getResultEnum());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handleException(Exception ex) {

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mEx = (MethodArgumentNotValidException) ex;
            return Result.makeResult(CodeEnum.PARAMS_ERROR)
                    .setMessage(mEx.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        }

        if (ex instanceof BindException) {
            BindException bEx = (BindException) ex;
            return Result.makeResult(CodeEnum.PARAMS_ERROR)
                    .setMessage(bEx.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        }

        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cEx = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violations = cEx.getConstraintViolations();
            ConstraintViolation<?> violation = violations.iterator().next();
            String message = violation.getMessage();
            return Result.makeResult(CodeEnum.PARAMS_ERROR)
                    .setMessage(message);
        }

        log.error("系统异常", ex);
        return Result.makeResult(CodeEnum.SYSTEM_ERROR);
    }
}
