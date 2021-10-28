package cn.handsome.core.micro.route.impl;

import cn.handsome.core.micro.route.RouterFinder;
import cn.handsome.core.micro.route.RouterRegister;
import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shoy
 * @date 2021/6/4
 */
@Slf4j
public class MemoryRouter implements RouterRegister, RouterFinder {
    private final ConcurrentMap<String, List<ServiceAddress>> serviceCache;

    public MemoryRouter() {
        this.serviceCache = new ConcurrentHashMap<>();
    }

    public MemoryRouter(Map<String, List<ServiceAddress>> services) {
        this.serviceCache = new ConcurrentHashMap<>(services);
    }

    @Override
    public List<ServiceAddress> find(String serviceName) {
        if (null != serviceName && serviceCache.containsKey(serviceName)) {
            return serviceCache.get(serviceName);
        }
        return null;
    }

    @Override
    public void clean(String serviceName) {
        if (CommonUtils.isNotEmpty(serviceName)) {
            serviceCache.remove(serviceName);
        }
    }

    @Override
    public void register(String serviceName, ServiceAddress address) {
        if (CommonUtils.isEmpty(serviceName)) {
            return;
        }
        log.info("[register]{} => {}", serviceName, address);
        List<ServiceAddress> addressList;
        if (serviceCache.containsKey(serviceName)) {
            addressList = serviceCache.get(serviceName);
            addressList.add(address);
            serviceCache.replace(serviceName, addressList);
        } else {
            addressList = new ArrayList<>();
            addressList.add(address);
            serviceCache.putIfAbsent(serviceName, addressList);
        }
    }

    @Override
    public void deregister() {
        serviceCache.clear();
    }
}
