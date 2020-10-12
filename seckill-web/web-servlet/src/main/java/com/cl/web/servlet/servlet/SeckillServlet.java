package com.cl.web.servlet.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CarterCL
 * @create 2020/10/12 13:25
 */
@WebServlet(urlPatterns = "/seckill",name = "seckillServlet")
public class SeckillServlet extends HttpServlet {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE_APPLICATION_JSON);

    }
}
