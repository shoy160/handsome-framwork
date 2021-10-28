package cn.handsome.core.proxy.impl;

import cn.handsome.core.proxy.ProxyFactory;
import cn.handsome.core.utils.IdentityUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Proxy Factory Impl
 *
 * @author shay
 * @date 2021/3/22
 */
@Slf4j
public class ProxyFactoryImpl implements ProxyFactory {
    private final ConcurrentMap<Class<?>, Map<InvocationHandler, Object>> proxyCache;

    public ProxyFactoryImpl() {
        this.proxyCache = new ConcurrentHashMap<>();
        log.info("proxy factory create,guid:{}", IdentityUtils.longId());
    }

    @Override
    public Object create(Class<?> clazz, InvocationHandler handler) {
        Map<InvocationHandler, Object> instanceMap = proxyCache.getOrDefault(clazz, null);
        if (null == instanceMap) {
            instanceMap = new HashMap<>();
        }
        if (instanceMap.containsKey(handler)) {
            return instanceMap.get(handler);
        }
        ClassLoader loader = clazz.getClassLoader();
        List<Class<?>> interfaces = new ArrayList<>();
        if (clazz.isInterface()) {
            interfaces.add(clazz);
        }
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
        Object instance = Proxy.newProxyInstance(loader, interfaces.toArray(new Class[0]), handler);
        instanceMap.put(handler, instance);
        if (proxyCache.containsKey(clazz)) {
            proxyCache.replace(clazz, instanceMap);
        } else {
            proxyCache.put(clazz, instanceMap);
        }
        return instance;
    }
}
