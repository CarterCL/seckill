package com.cl.web.servlet.utils;

/**
 * @author CarterCL
 * @create 2020/10/12 20:06
 */
public class StringUtils {

    public static boolean isBlank(String str){
        return str == null || str.trim().length() == 0;
    }

    private StringUtils(){}
}
