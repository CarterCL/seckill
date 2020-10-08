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
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void initUser() throws InterruptedException {


        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            List<Integer> ids = userMapper.listId(1000, 1000 * i);
            stringRedisTemplate.execute(new RedisCallback<Long>() {

                @Override
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.openPipeline();
                    for (Integer id : ids) {
                        String key = "USER:USER_ID:" + id;
                        redisConnection.set(key.getBytes(),"0".getBytes());
                    }
                    redisConnection.closePipeline();
                    return null;
                }
            });
        }
    }

    @Test
    public void initProduct() {

        List<Product> products = productMapper.listProductStock();
        for (Product product : products) {
            stringRedisTemplate.opsForValue().set("PRODUCT:PRODUCT_ID:"+product.getId(),product.getStock().toString());
        }
    }

}
