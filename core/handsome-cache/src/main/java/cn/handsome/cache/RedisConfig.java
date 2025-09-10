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
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shay
 * @date 2021/2/25
 */
@Slf4j
@EnableCaching
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Bean
    public RedisTemplateFactory redisTemplateFactory(RedisConnectionFactory connectionFactory) {
        return new RedisTemplateFactory(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public StringRedisTemplate stringRedisTemplate(RedisTemplateFactory factory) {
        return factory.stringRedisTemplate();
    }

    @Bean
    @Primary
    @SuppressWarnings("all")
    public <T> RedisTemplate<String, T> redisTemplate(RedisTemplateFactory factory) {
        return factory.redisTemplate();
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
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, CacheSyncListener cacheSyncListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        ChannelTopic topic = new ChannelTopic(Constants.CACHE_SYNC_CHANNEL);
        container.addMessageListener(cacheSyncListener, topic);
        return container;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisRouter redisRouter(StringRedisTemplate redisTemplate) {
        return new RedisRouter(redisTemplate);
    }

    @Bean
    public RedissonClient redissonClient(RedisProperties redisConfig) {
        return RedisLock.client(redisConfig);
    }

    @Bean
    public RedisLock redisLock(RedisProperties redisConfig) {
        return new RedisLock(redisConfig);
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
