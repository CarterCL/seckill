package com.cl.web.servlet.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author CarterCL
 * @create 2020/10/12 14:28
 */
public class RedisUtils {

    private static final RedisCommands<String, String> COMMANDS;

    private static final Map<String, AtomicReference<String>> LUA_SHA_MAP;


    public static String get(String key) {
        return COMMANDS.get(key);
    }

    public static String set(String key, String value) {
        return COMMANDS.set(key, value);
    }

    public static boolean hasKey(String key) {
        Long result = COMMANDS.exists(key);
        return result != null && (result != 0L);
    }

    public static String execute(String luaName, String luaContent, String[] keys, String[] args) {
        if (!LUA_SHA_MAP.containsKey(luaName)) {
            AtomicReference<String> atomicReference = new AtomicReference<>();
            atomicReference.compareAndSet(null, COMMANDS.scriptLoad(luaContent));
            LUA_SHA_MAP.put(luaName, atomicReference);
        }
        return COMMANDS.evalsha(LUA_SHA_MAP.get(luaName).get(), ScriptOutputType.VALUE, keys, args);
    }


    static {
        Properties properties = new Properties();
        try (
                InputStream inputStream = RedisUtils.class.getClassLoader().getResourceAsStream("redis.properties")
        ) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RedisURI redisURI = RedisURI.builder()
                .withHost(properties.getProperty("host"))
                .withPort(Integer.parseInt(properties.getProperty("port")))
                .withPassword(properties.getProperty("password"))
                .withDatabase(Integer.parseInt(properties.getProperty("database")))
                .build();
        RedisClient client = RedisClient.create(redisURI);
        StatefulRedisConnection<String,String> connection = client.connect();
        COMMANDS = connection.sync();

        LUA_SHA_MAP = new ConcurrentHashMap<>(16);
    }

    private RedisUtils() {
    }
}
