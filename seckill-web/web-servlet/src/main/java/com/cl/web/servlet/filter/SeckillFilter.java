package com.cl.web.servlet.filter;

import com.cl.base.enums.CodeEnum;
import com.cl.base.vo.Result;
import com.cl.web.servlet.utils.JsonUtils;
import com.cl.web.servlet.utils.RedisUtils;
import com.cl.web.servlet.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CarterCL
 * @create 2020/10/12 20:04
 */
@Slf4j
@WebFilter(urlPatterns = "/seckill")
public class SeckillFilter implements Filter {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String USER_KEY_PREFIX = "USER:USER_ID:";
    private static final String SECKILL_SUCCESS_PREFIX = "SECKILL:SUCCESS:";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setContentType(CONTENT_TYPE_APPLICATION_JSON);
        String userId = httpServletRequest.getParameter("userId");
        String productId = httpServletRequest.getParameter("productId");
        log.info(userId + "|" + productId);
        try {
            if (!checkParameters(userId, productId)) {
                httpServletResponse.getWriter().print(JsonUtils.serialize(Result.makeResult(CodeEnum.PARAMS_ERROR)));
                return;
            }

            if (!RedisUtils.hasKey(USER_KEY_PREFIX + userId)) {
                httpServletResponse.getWriter().print(JsonUtils.serialize(Result.makeResult(CodeEnum.USER_NOT_FOUND)));
                return;
            }

            if (RedisUtils.hasKey(SECKILL_SUCCESS_PREFIX + userId + productId)) {
                httpServletResponse.getWriter().print(JsonUtils.serialize(Result.makeResult(CodeEnum.ALREADY_INVOLVED)));
                return;
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            log.error("{}-{}|过滤器异常", userId, productId, ex);
        }
    }

    private static boolean checkParameters(String userId, String productId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(productId)) {
            return false;
        }

        if (userId.length() > 20 || productId.length() > 20) {
            return false;
        }
        return true;
    }
}
