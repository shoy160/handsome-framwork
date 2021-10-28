package cn.handsome.core.micro.route;

import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.utils.CommonUtils;

import java.util.List;

/**
 * 服务发现
 *
 * @author shoy
 * @date 2021/6/4
 */
public interface RouterFinder extends Router {
    /**
     * 服务发现
     *
     * @param serviceName 服务类型
     * @return list
     */
    List<ServiceAddress> find(String serviceName);

    /**
     * 清空服务缓存
     *
     * @param serviceName 服务名
     */
    void clean(String serviceName);

    /**
     * 服务发现
     *
     * @param serviceClazz 服务类型
     * @return list
     */
    default List<ServiceAddress> find(Class<?> serviceClazz) {
        String serviceName = getServiceName(serviceClazz);
        if (CommonUtils.isEmpty(serviceName)) {
            return null;
        }
        return find(serviceName);
    }

    /**
     * 清空服务缓存
     *
     * @param serviceClazz 服务类型
     */
    default void clean(Class<?> serviceClazz) {
        String serviceName = getServiceName(serviceClazz);
        if (CommonUtils.isEmpty(serviceName)) {
            return;
        }
        clean(serviceName);
    }
}
