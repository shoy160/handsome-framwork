package cn.handsome.core.cache;

import cn.handsome.core.Constants;
import cn.handsome.core.AppContext;
import cn.handsome.core.lang.Action;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.EncryptionUtil;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.TypeUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.core.util.StrUtil;

import java.util.concurrent.TimeUnit;

/**
 * 基础缓存类
 *
 * @author shay
 * @date 2021/03/06
 */
public abstract class BaseCache<K, V> implements Cache<K, V> {
    private final String region;

    protected BaseCache() {
        this(Constants.EMPTY_STR);
    }

    protected BaseCache(String region) {
        this.region = region;
    }

    @Override
    public long defaultExpired() {
        //随机1-10分钟
        int expired = RandomUtil.randomInt(60, 600);
        return System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expired);
    }

    @Override
    public String getRegion() {
        if (StrUtil.isBlank(this.region)) {
            //默认区域去 AppName
            String appName = AppContext.getAppName();
            if (StrUtil.isNotBlank(appName)) {
                return appName.replaceFirst("^handsome-", Constants.EMPTY_STR);
            }
        }
        return this.region;
    }

    @Override
    public void info() {
    }

    @Override
    public void keyExpired(Action<K> expiredAction, Class<K> clazz) {
    }

    protected String stringKey(K key) {
        return stringKey(key, true);
    }

    protected String stringKey(K key, boolean includeRegion) {
        if (null == key) {
            return Constants.EMPTY_STR;
        }
        String stringKey;
        if (TypeUtils.isSimple(key)) {
            stringKey = key.toString();
        } else {
            stringKey = JsonUtils.toJson(key);
        }
        if (includeRegion && StrUtil.isNotBlank(this.getRegion())) {
            return this.getRegion()
                    .concat(Constants.REGION_SPLIT)
                    .concat(stringKey);
        }
        return stringKey;
    }
}
