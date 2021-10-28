package cn.handsome.nacos.route;

import cn.handsome.core.AppContext;
import cn.handsome.core.cache.Cache;
import cn.handsome.core.cache.impl.MemoryCache;
import cn.handsome.core.enums.ServiceProtocol;
import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.micro.route.RouterFinder;
import cn.handsome.core.micro.route.RouterRegister;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.EnumUtils;
import cn.handsome.nacos.config.NacosProperties;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author shoy
 * @date 2021/6/28
 */
@Slf4j
public class NacosRouter implements RouterRegister, RouterFinder {
    private final static String KEY_PROTOCOL = "protocol";
    private final static String CACHE_REGION = "nacos";

    private final String serverAddr;
    private final String namespace;
    private final Cache<String, List<ServiceAddress>> cache;
    private final NamingService namingService;

    public NacosRouter(String serverAddr, String namespace) {
        this.serverAddr = serverAddr;
        this.namespace = namespace;
        this.namingService = createService();
        cache = new MemoryCache<>(CACHE_REGION);
    }

    public NacosRouter(NacosProperties config) {
        this(config.getServerAddr(), config.getNamespace());
    }

    private NamingService createService() {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", this.serverAddr);
        properties.setProperty("namespace", this.namespace);

        try {
            return NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            log.warn("Nacos服务发现异常", e);
            return null;
        }
    }

    private List<ServiceAddress> findInternal(String serviceName) {
        List<ServiceAddress> addressList = new ArrayList<>();
        if (null == namingService) {
            return addressList;
        }
        try {
            List<String> clusters = new ArrayList<>();
            clusters.add(AppContext.getAppMode());
            List<Instance> instances = namingService.selectInstances(serviceName, clusters, true);
            for (Instance instance : instances) {
                ServiceAddress address = new ServiceAddress(instance.getIp(), instance.getPort());
                address.setService(instance.getIp());
                address.setWeight((int) instance.getWeight());
                Map<String, String> metadata = instance.getMetadata();
                String protocolName = metadata.get(KEY_PROTOCOL);
                ServiceProtocol protocol = EnumUtils.getEnum(protocolName, ServiceProtocol.class);
                address.setProtocol(protocol);
                addressList.add(address);
            }
            return addressList;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ServiceAddress> find(String serviceName) {
        return cache.getOrPut(serviceName, this::findInternal);
    }

    @Override
    public void clean(String serviceName) {
        cache.clean();
    }

    @Override
    public void register(String serviceName, ServiceAddress address) {
        if (null == namingService) {
            return;
        }
        try {
            if (StrUtil.isEmpty(address.getService())) {
                return;
            }
            String mode = AppContext.getAppMode();
            Instance instance = new Instance();
            instance.setHealthy(address.isHealthy());
            instance.setEphemeral(false);
            instance.setClusterName(mode);
            instance.setIp(address.getService());
            instance.setPort(address.getServicePort());
            instance.setWeight(address.getWeight());
            Map<String, String> instanceMeta = new HashMap<>(1);
            instanceMeta.put(KEY_PROTOCOL, address.getProtocol().getName());
            instance.setMetadata(instanceMeta);
            namingService.registerInstance(serviceName, instance);
            log.info("[nacos register]:{}", instance);
            List<ServiceAddress> addressList = cache.get(serviceName);
            if (null == addressList) {
                addressList = new ArrayList<>();
            }
            addressList.add(address);
            cache.put(serviceName, addressList);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deregister() {
        if (CommonUtils.isEmpty(cache) || null == namingService) {
            return;
        }
        for (String key : cache.keySet(String.class)) {
            for (ServiceAddress address : cache.get(key)) {
                try {
                    namingService.deregisterInstance(key, address.getService(), address.getPort(), namespace);
                    log.info("[nacos deregister]:{}", address);
                } catch (NacosException e) {
                    e.printStackTrace();
                }
            }
        }
        cache.clean();
    }
}
