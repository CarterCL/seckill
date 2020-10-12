package com.cl.web.servlet.servlet;

import com.cl.base.enums.CodeEnum;
import com.cl.base.vo.Result;
import com.cl.web.servlet.utils.JsonUtils;
import com.cl.web.servlet.utils.RedisUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CarterCL
 * @create 2020/10/12 13:25
 */
@WebServlet(urlPatterns = "/seckill", name = "seckillServlet")
public class SeckillServlet extends HttpServlet {
    private static final String PRODUCT_KEY_PREFIX = "PRODUCT:PRODUCT_ID:";
    private static final String USER_KEY_PREFIX = "USER:USER_ID:";
    private static final String SECKILL_SUCCESS_KEY_PREFIX = "SECKILL:SUCCESS:";
    private static final Long SECKILL_SUCCESS = 1L;
    private static final Map<Integer, String> SELL_OUT_PRODUCT_MAP = new ConcurrentHashMap<>(16);

    private static final String LUA_NAME = "seckill";

    private static String LUA_CONTENT;

    static {
        try (
                InputStream inputStream = SeckillServlet.class.getClassLoader().getResourceAsStream("seckill.lua")
        ) {
            assert inputStream != null;
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            LUA_CONTENT = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String userId = req.getParameter("userId");
        Integer productId = Integer.valueOf(req.getParameter("productId"));

        if (SELL_OUT_PRODUCT_MAP.containsKey(productId)) {
            printResponse(resp, CodeEnum.SELL_OUT);
            return;
        }

        if (!RedisUtils.hasKey(PRODUCT_KEY_PREFIX + productId)) {
            printResponse(resp, CodeEnum.PRODUCT_NOT_FOUND);
            return;
        }

        String[] keyArr = new String[3];
        keyArr[0] = USER_KEY_PREFIX + userId;
        keyArr[1] = PRODUCT_KEY_PREFIX + productId;
        keyArr[2] = SECKILL_SUCCESS_KEY_PREFIX + userId + productId;

        String result = RedisUtils.execute(LUA_NAME, LUA_CONTENT, keyArr, new String[]{});
        if (result == null || !SECKILL_SUCCESS.equals(Long.valueOf(result))) {
            SELL_OUT_PRODUCT_MAP.put(productId, "");
            printResponse(resp, CodeEnum.SELL_OUT);
            return;
        }
        printResponse(resp, CodeEnum.SUCCESS);

    }

    private static void printResponse(HttpServletResponse response, CodeEnum codeEnum) throws IOException {
        response.getWriter().print(JsonUtils.serialize(Result.makeResult(codeEnum)));
    }
}
