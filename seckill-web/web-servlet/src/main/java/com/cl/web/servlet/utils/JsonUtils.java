package com.cl.web.servlet.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author CarterCL
 * @create 2020/10/12 20:08
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String serialize(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static <T> T deserialize(String str, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(str, clazz);
    }

    private JsonUtils() {
    }
}
