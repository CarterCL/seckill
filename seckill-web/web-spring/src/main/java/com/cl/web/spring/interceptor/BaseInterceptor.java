package com.cl.web.spring.interceptor;

import com.cl.web.base.enums.IEnum;
import com.cl.web.base.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author CarterCL
 * @create 2020/9/30 21:34
 */
@Slf4j
public class BaseInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    protected void print2Response(IEnum resultEnum, HttpServletResponse response) {
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setHeader("Content-Type","application/json;charset=UTF-8");
            response.getWriter().println(objectMapper.writeValueAsString(Result.makeResult(resultEnum)));
        } catch (Exception e) {
            log.error("BaseInterceptor-print2Response 异常", e);
        }
    }

    public BaseInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
