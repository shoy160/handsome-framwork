package cn.handsome.cache;

import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.gcode.*;
import cn.handsome.core.utils.JsonUtils;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 全局编码辅助类
 *
 * @author shoy
 * @date 2021/6/30
 */
@Slf4j
@RequiredArgsConstructor
public class RedisGlobalCode implements GlobalCode {
    private final StringRedisTemplate redisTemplate;
    private final RedisLock redisLock;
    private static final String KEY_RULE_FORMAT = "gcode:rule:%s";
    private static final String KEY_STORE_FORMAT = "gcode:store:%s";
    private static final String KEY_CODE_FORMAT = "gcode:codes:%s";

    private <T> T getCache(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return JsonUtils.json(value, clazz);
    }

    private CodeRule getRule(String name) {
        String key = String.format(KEY_RULE_FORMAT, name);
        CodeRule rule = getCache(key, CodeRule.class);
        if (null == rule) {
            throw new BusinessException("全局编码规则未注册");
        }
        return rule;
    }

    private void checkCode(String name, CodeRule rule) {
        String key = String.format(KEY_CODE_FORMAT, name);
        Long size = redisTemplate.opsForSet().size(key);
        if (null == size || size < rule.getMinStock()) {
            fill(name, rule.getSupplyCount());
        }
    }

    @Override
    public void register(String name, CodeRule rule) {
        String key = String.format(KEY_RULE_FORMAT, name);
        redisTemplate.opsForValue().set(key, JsonUtils.toJson(rule));
        checkCode(name, rule);
    }

    private void fillInternal(String name, int count) {
        CodeRule rule = getRule(name);
        String storeKey = String.format(KEY_STORE_FORMAT, name);
        String codeKey = String.format(KEY_CODE_FORMAT, name);
        RLock lock = redisLock.getLock(storeKey.concat(":lock"));
        try {
            if (lock.tryLock()) {
                String[] codes = new String[count];
                int index = 0;
                do {
                    String code = CodeHelper.random(rule);
                    Boolean member = redisTemplate.opsForSet().isMember(storeKey, code);
                    if (null == member || !member) {
                        codes[index] = code;
                        index++;
                    }
                } while (index < count);
                redisTemplate.opsForSet().add(storeKey, codes);
                redisTemplate.opsForSet().add(codeKey, codes);
                if (rule.getType() == CodeType.DateFormat) {
                    Date expire = CodeHelper.expire(rule.getFormat());
                    redisTemplate.expireAt(storeKey, expire);
                    redisTemplate.expireAt(codeKey, expire);
                }
                log.info("GlobalCode [{}] filled {} counts", name, count);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void fill(String name, int count) {
        ThreadFactory factory = ThreadFactoryBuilder.create().setNamePrefix("gcode-").build();
        ThreadPoolExecutor executor = ExecutorBuilder.create().setThreadFactory(factory).setMaxPoolSize(1).build();
        executor.execute(() -> fillInternal(name, count));
    }

    @Override
    public String code(String name) {
        CodeRule rule = getRule(name);
        String key = String.format(KEY_CODE_FORMAT, name);
        return redisLock.tryLockT(key.concat(":lock"), () -> {
            while (true) {
                String value = redisTemplate.opsForSet().pop(key);
                if (StrUtil.isNotBlank(value)) {
                    checkCode(name, rule);
                    return value;
                }
                fill(name, rule.getSupplyCount());
            }
        });
    }

    @Override
    public void recovery(String name, String code) {
        String storeKey = String.format(KEY_STORE_FORMAT, name);
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        Boolean member = ops.isMember(storeKey, code);
        if (null == member || !member) {
            throw new BusinessException("该编码尚未入库");
        }
        String codeKey = String.format(KEY_CODE_FORMAT, name);
        ops.add(codeKey, code);
    }

    @Override
    public void used(String name, String... codes) {
        String key = String.format(KEY_CODE_FORMAT, name);
        String storeKey = String.format(KEY_STORE_FORMAT, name);
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        ops.remove(key, Arrays.stream(codes).map(Object.class::cast).toArray());
        ops.add(storeKey, codes);
    }

    @Override
    public CodeInfo info(String name) {
        CodeRule rule = getRule(name);
        CodeInfo info = new CodeInfo();
        info.setName(name);
        info.setRule(rule);
        String storeKey = String.format(KEY_STORE_FORMAT, name);
        info.setTotal(redisTemplate.opsForSet().size(storeKey));
        String codeKey = String.format(KEY_CODE_FORMAT, name);
        info.setLeft(redisTemplate.opsForSet().size(codeKey));
        return info;
    }
}
