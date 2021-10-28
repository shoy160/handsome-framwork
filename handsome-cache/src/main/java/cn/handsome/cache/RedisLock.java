package cn.handsome.cache;

import cn.handsome.core.lang.ActionVoid;
import cn.handsome.core.lang.FuncVoid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author shoy
 * @date 2021/6/30
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLock {
    private final RedissonClient redissonClient;

    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    public void tryLock(String key, ActionVoid action) {
        tryLock(key, -1, action);
    }

    public void tryLock(String key, long timeout, ActionVoid action) {
        if (null == action) {
            return;
        }
        RLock lock = redissonClient.getLock(key);
        try {
            boolean result;
            if (timeout > 0) {
                result = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
            } else {
                result = lock.tryLock();
            }
            if (result) {
                action.invoke();
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
        RLock lock = redissonClient.getLock(key);
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
}
