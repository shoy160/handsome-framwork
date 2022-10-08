package cn.handsome.cache;

import cn.handsome.core.cache.BaseCache;
import cn.handsome.core.cache.SyncCacheKey;
import cn.handsome.core.cache.impl.MemoryCache;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.function.Function;

/**
 * @author shoy
 * @date 2022/9/30
 */
@Slf4j
public class CacheFactory<K, V> extends BaseCache<K, V> {
    private final String id;
    private final MemoryCache<K, V> firstCache;
    private final RedisCache<K, V> secondCache;

    public CacheFactory(String region, MemoryCache<K, V> firstCache, RedisCache<K, V> secondCache) {
        super((region));
        this.id = IdUtil.fastSimpleUUID();
        this.firstCache = firstCache;
        this.secondCache = secondCache;
    }

    public String getFactoryId() {
        return this.id;
    }

    @Override
    public V get(K key) {
        V value = firstCache.get(key);
        if (null != value) {
            log.debug("get cache value from memory -> {}", key);
            return value;
        }
        if (null != secondCache) {
            value = secondCache.get(key);
            if (null != value) {
                log.debug("get cache from redis -> {}", key);
                firstCache.put(key, value);
                return value;
            }
        }
        return null;
    }

    @Override
    public V getOrPut(K key, Function<? super K, ? extends V> valueFunc, long expireDate) {
        return null;
    }

    @Override
    public void put(K key, V value, long expireDate) {
        if (null != secondCache) {
            firstCache.put(key, value);
            secondCache.put(key, value, expireDate);
            sendSync(key);
        } else {
            firstCache.put(key, value, expireDate);
        }
    }

    @Override
    public void remove(K key) {
        firstCache.remove(key);
        if (null != secondCache) {
            secondCache.remove(key);
            sendSync(key);
        }
    }

    @Override
    public Set<K> keySet(Class<K> clazz) {
        return null;
    }

    @Override
    public void clean() {

    }

    public void cleanMemory(K key) {
        firstCache.remove(key);
    }

    private void sendSync(K key) {
        if (null == secondCache) {
            return;
        }
        SyncCacheKey cacheKey = new SyncCacheKey(getRegion(), stringKey(key), this.id);
        secondCache.sendSync(cacheKey);
    }
}
