package com.cl.web.spring.interceptor;

import com.cl.base.enums.CodeEnum;
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

    private static final String USER_KEY_PREFIX = "USER:USER_ID:";
    private static final String SECKILL_SUCCESS_PREFIX = "SECKILL:SUCCESS:";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String userId = request.getParameter("userId");
        String productId = request.getParameter("productId");
        if (StringUtils.isEmpty(userId) || userId.length() > 20) {
            print2Response(CodeEnum.PARAMS_ERROR, response);
            return false;
        }
        if (StringUtils.isEmpty(productId) || productId.length() > 20) {
            print2Response(CodeEnum.PARAMS_ERROR, response);
            return false;
        }

        try {
            Boolean hasUser = stringRedisTemplate.hasKey(USER_KEY_PREFIX + userId);
            if (hasUser == null || !hasUser) {
                print2Response(CodeEnum.USER_NOT_FOUND, response);
                return false;
            }

            Boolean isSeckillSuccess = stringRedisTemplate.hasKey(SECKILL_SUCCESS_PREFIX + userId + productId);
            if(isSeckillSuccess != null && isSeckillSuccess){
                print2Response(CodeEnum.ALREADY_INVOLVED, response);
                return false;
            }
        } catch (Exception ex) {
            log.error("SeckillInterceptor 异常", ex);
            print2Response(CodeEnum.SYSTEM_BUSY, response);
            return false;
        }

        return true;
    }

    public SeckillInterceptor(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        super(objectMapper);
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
