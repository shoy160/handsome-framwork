package cn.handsome.core.micro.route;

import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务注册
 *
 * @author shoy
 * @date 2021/6/4
 */
public interface RouterRegister extends Router {
    /**
     * 服务注册
     *
     * @param services 服务列表
     * @param address  服务地址
     */
    default void register(List<Class<?>> services, ServiceAddress address) {
        if (CommonUtils.isEmpty(services)) {
            return;
        }
        List<String> serviceIds = new ArrayList<>();
        for (Class<?> service : services) {
            String serviceId = getServiceName(service);
            if (CommonUtils.isNotEmpty(serviceId) && !serviceIds.contains(serviceId)) {
                register(serviceId, address);
                serviceIds.add(serviceId);
            }
        }
    }

    /**
     * 服务注册
     *
     * @param serviceName 服务名
     * @param address     服务地址
     */
    void register(String serviceName, ServiceAddress address);

    /**
     * 注销服务注册
     */
    void deregister();
}
