package com.cl.web.spring.interceptor;

import com.cl.web.base.enums.CodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CarterCL
 * @create 2020/9/30 21:32
 */
@Slf4j
public class SeckillInterceptor extends BaseInterceptor {

    private static final String USER_KEY_PREFIX = "USER_";
    private static final String IS_SECKILL_SUCCESS = "1";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String username = request.getParameter("username");
        String productId = request.getParameter("productId");
        if (!StringUtils.isEmpty(username) && username.length() > 20) {
            print2Response(CodeEnum.PARAMS_ERROR, response);
            return false;
        }
        if (!StringUtils.isEmpty(productId) && productId.length() > 20) {
            print2Response(CodeEnum.PARAMS_ERROR, response);
            return false;
        }

        String userSeckillState = null;
        try {
            userSeckillState = stringRedisTemplate.opsForValue().get(USER_KEY_PREFIX + username);

        } catch (Exception ex) {
            log.error("SeckillInterceptor 异常", ex);
        }

        if (StringUtils.isEmpty(userSeckillState)) {
            print2Response(CodeEnum.USER_NOT_FOUND, response);
            return false;
        }

        if (IS_SECKILL_SUCCESS.equals(userSeckillState)) {
            print2Response(CodeEnum.ALREADY_INVOLVED, response);
            return false;
        }

        return true;
    }

    public SeckillInterceptor(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        super(objectMapper);
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
