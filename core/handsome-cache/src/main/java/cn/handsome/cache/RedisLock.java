package cn.handsome.cache;

import cn.handsome.core.lang.ActionVoid;
import cn.handsome.core.lang.FuncVoid;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.io.IOException;
import java.rmi.ConnectIOException;
import java.util.concurrent.TimeUnit;

/**
 * @author shoy
 * @date 2021/6/30
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLock {
    private final RedisProperties redisConfig;
    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        if (null == redissonClient) {
            redissonClient = client(this.redisConfig);
        }
        return this.redissonClient;
    }

    public RLock getLock(String key) {
        return getRedissonClient().getLock(key);
    }

    public void tryLock(String key, Runnable action) {
        tryLock(key, -1, action);
    }

    public void tryLock(String key, long timeout, Runnable action) {
        if (null == action || null == getRedissonClient()) {
            return;
        }
        RLock lock = getRedissonClient().getLock(key);
        try {
            boolean result;
            if (timeout > 0) {
                result = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
            } else {
                result = lock.tryLock();
            }
            if (result) {
                action.run();
            }
        } catch (InterruptedException ex) {
            log.warn("获取分布式锁失败：{}", ex.getLocalizedMessage());
        } finally {
            lock.unlock();
        }
    }

    public <T> T tryLockT(String key, FuncVoid<T> func) {
        return tryLockT(key, -1, func);
    }

    public <T> T tryLockT(String key, long timeout, FuncVoid<T> func) {
        if (null == func) {
            return null;
        }
        RLock lock = getRedissonClient().getLock(key);
        try {
            boolean result;
            if (timeout > 0) {
                result = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
            } else {
                result = lock.tryLock();
            }
            if (result) {
                return func.invoke();
            }
        } catch (InterruptedException ex) {
            log.warn("获取分布式锁失败：{}", ex.getLocalizedMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }

    public static RedissonClient client(RedisProperties redisConfig) {
        if (null == redisConfig || StrUtil.isBlank(redisConfig.getHost())) {
            return null;
        }
        try {
            String address = String.format("redis://%s:%d", redisConfig.getHost(), redisConfig.getPort());
            Config config = new Config();
            SingleServerConfig serversConfig = config.useSingleServer().setAddress(address);
            if (StrUtil.isNotBlank(redisConfig.getPassword())) {
                serversConfig.setPassword(redisConfig.getPassword());
                serversConfig.setDatabase(redisConfig.getDatabase());
            }
            return Redisson.create(config);
        } catch (Exception ex) {
            log.error("create redisson client error:{}", ex.getMessage());
            return null;
        }
    }
}
