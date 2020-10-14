package com.cl.order.listener;

import com.cl.order.dto.SeckillDTO;
import com.cl.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author CarterCL
 * @create 2020/10/7 22:37
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisStreamListener implements StreamListener<String, MapRecord<String,String,String>> {

    @Value("${spring.redis.stream.name}")
    private String name;

    private static final String USER_ID_KEY = "userId";
    private static final String PRODUCT_ID_KEY = "productId";

    private final OrderService orderService;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {

        log.info("收到消息:"+message.getId().getValue());
        Map<String,String> body = message.getValue();
        if(body.size() != 2){
            return;
        }
        SeckillDTO seckillDTO = new SeckillDTO();
        seckillDTO.setUserId(Integer.parseInt(body.get(USER_ID_KEY).split(":")[2]));
        seckillDTO.setProductId(Integer.parseInt(body.get(PRODUCT_ID_KEY).split(":")[2]));
        orderService.createOrder(seckillDTO);

        stringRedisTemplate.opsForStream().acknowledge(name, message);
    }
}
