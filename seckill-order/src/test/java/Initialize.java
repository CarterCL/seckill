import com.cl.base.entity.Product;
import com.cl.order.SeckillOrderApplication;
import com.cl.order.mapper.ProductMapper;
import com.cl.order.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author CarterCL
 * @create 2020/10/8 19:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillOrderApplication.class)
public class Initialize {

    private static final boolean INIT_USER = true;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void initUser() {

        /**
         * 第一次运行需要初始化用户，后边不需要运行此方法
         */
        if (!INIT_USER) {
            return;
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            List<Integer> ids = userMapper.listId(1000, 1000 * i);
            stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> {
                redisConnection.openPipeline();
                for (Integer id : ids) {
                    String key = "USER:USER_ID:" + id;
                    redisConnection.set(key.getBytes(), "0".getBytes());
                }
                redisConnection.closePipeline();
                return null;
            });
        }
    }

    @Test
    public void initProduct() {

        productMapper.initStock(1, 100);
        productMapper.initStock(2, 100);
        productMapper.initStock(3, 100);

        List<Product> products = productMapper.listProductStock();
        for (Product product : products) {
            stringRedisTemplate.opsForValue().set("PRODUCT:PRODUCT_ID:" + product.getId(),
                    product.getStock().toString());
        }
    }

    @Test
    public void initMq() {
        String orderMqKey = "ordermq";
        stringRedisTemplate.delete(orderMqKey);
        Set<String> keys = stringRedisTemplate.keys("SECKILL:SUCCESS*");
        stringRedisTemplate.delete(keys);

        Map<String,String> fieldMap = new HashMap<>();
        fieldMap.put("test", "test");
        MapRecord<String, String, String> mapRecord =
                StreamRecords.mapBacked(fieldMap).withStreamKey(orderMqKey).withId(RecordId.autoGenerate());
        stringRedisTemplate.opsForStream().add(mapRecord);
        stringRedisTemplate.opsForStream().createGroup(orderMqKey,ReadOffset.latest(),"group-order");
    }

}
