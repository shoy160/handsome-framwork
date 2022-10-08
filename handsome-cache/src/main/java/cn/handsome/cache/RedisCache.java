package cn.handsome.cache;

import cn.handsome.core.cache.SyncCacheKey;
import cn.hutool.core.convert.Convert;
import cn.handsome.core.Constants;
import cn.handsome.core.cache.BaseCache;
import cn.handsome.core.lang.Action;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author shay
 * @date 2021/2/25
 */
@Slf4j
public class RedisCache<K, V> extends BaseCache<K, V> {
    private final RedisTemplate<String, V> template;
    private final RedisKeyExpirationListener expirationListener;
    private final static String IGNORE_MATCH = "^";

    public RedisCache(RedisTemplate<String, V> template) {
        this(template, Constants.EMPTY_STR);
    }

    public RedisCache(RedisTemplate<String, V> template, String region) {
        this(template, null, region);
    }

    public RedisCache(RedisTemplate<String, V> template, RedisKeyExpirationListener expirationListener) {
        this(template, expirationListener, Constants.EMPTY_STR);

    }

    public RedisCache(RedisTemplate<String, V> template, RedisKeyExpirationListener expirationListener, String region) {
        super(region);
        this.template = template;
        this.expirationListener = expirationListener;
    }

    private String getKey(K key) {
        if (key == null) {
            return Constants.EMPTY_STR;
        }
        String stringKey;
        Class<?> clazz = key.getClass();
        if (TypeUtils.isSimple(clazz)) {
            stringKey = key.toString();
        } else {
            stringKey = JsonUtils.toJson(key);
        }
        if (CommonUtils.isEmpty(this.getRegion())) {
            return stringKey;
        }
        if (stringKey.startsWith(IGNORE_MATCH)) {
            return stringKey.substring(1);
        }
        return String.format("%s:%s", this.getRegion(), stringKey);
    }

    private K convertKey(String key, Class<K> clazz) {
        String region = this.getRegion();
        key = key.replaceAll(String.format("^%s:", region), Constants.EMPTY_STR);
        if (TypeUtils.isSimple(clazz)) {
            return Convert.convert(clazz, key);
        } else {
            return JsonUtils.json(key, clazz);
        }
    }

    @Override
    public void put(K key, V value, long expireDate) {
        long timestamp = System.currentTimeMillis();
        if (expireDate > timestamp) {
            template.opsForValue().set(getKey(key), value, expireDate - timestamp, TimeUnit.MILLISECONDS);
        } else if (expireDate < 0) {
            template.opsForValue().set(getKey(key), value);
        }
    }

    @Override
    public V get(K key) {
        String cacheKey = getKey(key);
        log.debug("redis get: {}", cacheKey);
        return key == null ? null : template.opsForValue().get(cacheKey);
    }

    @Override
    public V getOrPut(K key, Function<? super K, ? extends V> valueFunc, long expireDate) {
        V value = get(key);
        if (value == null && valueFunc != null) {
            value = valueFunc.apply(key);
            put(key, value, expireDate);
        }
        return value;
    }

    @Override
    public void remove(K key) {
        template.delete(getKey(key));
    }

    @Override
    public Set<K> keySet(Class<K> clazz) {
        String region = this.getRegion();
        String pattern = CommonUtils.isEmpty(region) ? "*" : region.concat(":*");
        Set<String> keys = template.keys(pattern);
        Set<K> keySet = new HashSet<>();
        if (null == keys) {
            return keySet;
        }
        for (String key : keys) {
            keySet.add(convertKey(key, clazz));
        }
        return keySet;
    }

    @Override
    public void clean() {
        String region = this.getRegion();
        String pattern = CommonUtils.isEmpty(region) ? "*" : region.concat(":*");
        Set<String> keys = template.keys(pattern);
        if (keys != null) {
            template.delete(keys);
        }
    }


    @Override
    public void keyExpired(Action<K> expiredAction, Class<K> clazz) {
        if (expirationListener == null) {
            return;
        }
        expirationListener.setAction(key -> {
            String region = this.getRegion();
            if (CommonUtils.isEmpty(region) || key.startsWith(region.concat(":"))) {
                expiredAction.invoke(convertKey(key, clazz));
            }
        });
    }

    public void sendSync(SyncCacheKey cacheKey) {
        this.template.convertAndSend(Constants.CACHE_SYNC_CHANNEL, cacheKey);
    }
}
