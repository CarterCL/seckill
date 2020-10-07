package com.cl.order.runner;

import com.cl.order.listener.RedisStreamListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author CarterCL
 * @create 2020/10/7 22:53
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisStreamConsumerRunner implements ApplicationRunner, DisposableBean {

    @Value("${spring.redis.stream.group}")
    private String group;
    @Value("${spring.redis.stream.consumer}")
    private String consumer;
    @Value("${spring.redis.stream.name}")
    private String name;

    private final RedisConnectionFactory redisConnectionFactory;

    private final RedisStreamListener redisStreamListener;

    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;

    @Override
    public void run(ApplicationArguments args) {
        StreamMessageListenerContainerOptions<String, MapRecord<String,String,String>> options = StreamMessageListenerContainerOptions
                .builder()
                .batchSize(2)
                .executor(new ThreadPoolExecutor(1,2,60, TimeUnit.MINUTES,new ArrayBlockingQueue<>(10)))
                .errorHandler(throwable -> log.error("Redis Stream监听异常",throwable))
                .pollTimeout(Duration.ZERO)
                .serializer(new StringRedisSerializer())
                .build();
        StreamMessageListenerContainer<String, MapRecord<String,String,String>> container = StreamMessageListenerContainer
                .create(redisConnectionFactory, options);
        container.receive(Consumer.from(group,consumer), StreamOffset.create(name, ReadOffset.lastConsumed()),redisStreamListener);
        this.container = container;
        this.container.start();
    }

    @Override
    public void destroy() {
        this.container.stop();
    }
}
