package com.cl.web.spring.service;

import com.cl.base.dto.SeckillDTO;
import com.cl.base.enums.CodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CarterCL
 * @create 2020/9/29 21:41
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SeckillService {

    private static final String PRODUCT_KEY_PREFIX = "PROD_";
    private static final String USER_KEY_PREFIX = "USER_";
    private static final String SECKILL_SUCCESS_KEY_PREFIX = "SUCCESS_";
    private static final Long SECKILL_SUCCESS = 1L;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    private static final Map<Integer, String> SELL_OUT_PRODUCT_MAP = new ConcurrentHashMap<>(16);

    private final RedisTemplate<String, Object> redisTemplate;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/seckill.lua")));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    /**
     * 秒杀
     * @param seckillDTO 用户名、商品ID
     * @return 秒杀结果
     */
    public CodeEnum seckill(SeckillDTO seckillDTO) {

        // 取本地缓存商品是否售空
        if (SELL_OUT_PRODUCT_MAP.containsKey(seckillDTO.getProductId())) {
            return CodeEnum.SELL_OUT;
        }
        // 判断商品是否存在
        Boolean isHasProduct = redisTemplate.hasKey(PRODUCT_KEY_PREFIX + seckillDTO.getProductId());

        if (isHasProduct == null || !isHasProduct) {
            return CodeEnum.PRODUCT_NOT_FOUND;
        }

        List<String> keyList = new ArrayList<>(3);
        keyList.add(USER_KEY_PREFIX + seckillDTO.getUserId());
        keyList.add(PRODUCT_KEY_PREFIX + seckillDTO.getProductId());
        keyList.add(SECKILL_SUCCESS_KEY_PREFIX + seckillDTO.getUserId() + seckillDTO.getProductId());

        // 执行lua脚本，进行秒杀
        Long result = redisTemplate.execute(SECKILL_SCRIPT, keyList);
        if (!SECKILL_SUCCESS.equals(result)) {
            SELL_OUT_PRODUCT_MAP.put(seckillDTO.getProductId(), "");
            return CodeEnum.SELL_OUT;
        }
        return CodeEnum.SUCCESS;
    }
}
