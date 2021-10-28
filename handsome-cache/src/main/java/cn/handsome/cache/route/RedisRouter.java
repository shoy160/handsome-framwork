package cn.handsome.cache.route;

import cn.handsome.core.Context;
import cn.handsome.core.cache.impl.MemoryCache;
import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.micro.route.RouterFinder;
import cn.handsome.core.micro.route.RouterRegister;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author shoy
 * @date 2021/6/8
 */
@Slf4j
@RequiredArgsConstructor
public class RedisRouter implements RouterRegister, RouterFinder {
    private final StringRedisTemplate redisTemplate;
    private final MemoryCache<String, List<ServiceAddress>> memoryCache;

    public RedisRouter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        memoryCache = new MemoryCache<>("thrift");
    }

    private static String serviceKey(String name) {
        String mode = Context.isProd() ? "prod" : (Context.isTest() ? "test" : "dev");
        return String.format("thrift:%s:%s", mode, name);
    }

    @Override
    public List<ServiceAddress> find(String serviceName) {
        return memoryCache.getOrPut(serviceName, name -> {
            List<ServiceAddress> addressList = new ArrayList<>();
            String key = serviceKey(name);
            Set<String> members = redisTemplate.opsForSet().members(key);
            if (members != null) {
                for (String member : members) {
                    addressList.add(JsonUtils.json(member, ServiceAddress.class));
                }
            }
            return addressList;
        }, 30, TimeUnit.SECONDS);
    }

    @Override
    public void clean(String serviceName) {
        String key = serviceKey(serviceName);
        redisTemplate.opsForSet().remove(key);
    }

    @Override
    public void register(String serviceName, ServiceAddress address) {
        if (address == null || CommonUtils.isEmpty(address.getService())) {
            return;
        }
        String key = serviceKey(serviceName);
        log.info("[register]{} => {}", key, address);
        redisTemplate.opsForSet().add(key, JsonUtils.toJson(address));
    }

    @Override
    public void deregister() {
    }
}
