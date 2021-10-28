package cn.handsome.cache;

import cn.handsome.cache.route.RedisRouter;
import cn.handsome.core.Constants;
import cn.handsome.core.WorkerIdSolver;
import cn.handsome.core.cache.Cache;
import cn.handsome.core.gcode.GlobalCode;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shay
 * @date 2021/2/25
 */
@EnableCaching
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private static RedisSerializer<Object> objectRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(om);
        return serializer;
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    @Primary
    @SuppressWarnings("all")
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> template = new RedisTemplate<String, T>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        RedisSerializer<Object> valueSerializer = objectRedisSerializer();
        // value序列化方式采用jackson
        template.setValueSerializer(valueSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    public Cache<String, String> stringCacheBean(StringRedisTemplate template) {
        return new RedisCache<>(template);
    }

    @Bean
    @SuppressWarnings("all")
    public <T> Cache<String, T> stringTCacheBean(RedisTemplate<String, T> template) {
        return new RedisCache<>(template);
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisRouter routerFinder(StringRedisTemplate redisTemplate) {
        return new RedisRouter(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedisProperties redisConfig) {
        String address = String.format("redis://%s:%d", redisConfig.getHost(), redisConfig.getPort());
        Config config = new Config();
        SingleServerConfig serversConfig = config.useSingleServer().setAddress(address);
        if (StrUtil.isNotBlank(redisConfig.getPassword())) {
            serversConfig.setPassword(redisConfig.getPassword());
            serversConfig.setDatabase(redisConfig.getDatabase());
        }
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisLock redisLock(RedissonClient redissonClient) {
        return new RedisLock(redissonClient);
    }

    @Bean
    @Primary
    @Profile({Constants.MODE_TEST, Constants.MODE_READY, Constants.MODE_PROD})
    public WorkerIdSolver redisWorkerId(StringRedisTemplate redisTemplate) {
        return () -> {
            long workerIdBits = 5L;
            final String cacheKeyWorkerId = "framework:worker_id";
            Long workId = redisTemplate.opsForValue().increment(cacheKeyWorkerId);
            if (null == workId || workId >= 1L << workerIdBits) {
                workId = 0L;
                redisTemplate.opsForValue().set(cacheKeyWorkerId, workId.toString());
            }
            return workId;
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalCode globalCode(StringRedisTemplate redisTemplate, RedisLock redisLock) {
        return new RedisGlobalCode(redisTemplate, redisLock);
    }

}
