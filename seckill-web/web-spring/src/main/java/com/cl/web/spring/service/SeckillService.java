package com.cl.web.spring.service;

import com.cl.web.base.dto.SeckillDTO;
import com.cl.web.base.enums.CodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CarterCL
 * @create 2020/9/29 21:41
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SeckillService {

    private static final String PRODUCT_KEY_PREFIX = "PROD_";
    private static final String USER_KEY_PREFIX = "USER_";
    private static final Long SECKILL_SUCCESS = 1L;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    private final RedisTemplate<String, Object> redisTemplate;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/seckill.lua")));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    public CodeEnum seckill(SeckillDTO seckillDTO) {

        Boolean isHasProduct = redisTemplate.hasKey(PRODUCT_KEY_PREFIX + seckillDTO.getProductId());

        if (isHasProduct == null || !isHasProduct) {
            return CodeEnum.PRODUCT_NOT_FOUND;
        }

        List<String> keyList = new ArrayList<>(2);
        keyList.add(USER_KEY_PREFIX + seckillDTO.getUsername());
        keyList.add(PRODUCT_KEY_PREFIX + seckillDTO.getProductId());

        Long result = redisTemplate.execute(SECKILL_SCRIPT, keyList);
        if(!SECKILL_SUCCESS.equals(result)){
            return CodeEnum.SELL_OUT;
        }
        return CodeEnum.SUCCESS;
    }
}
