package cn.handsome.core.micro.route;

/**
 * @author shoy
 * @date 2021/6/4
 */
public interface Router {
    /**
     * 获取服务名
     *
     * @param serviceClazz 服务类型
     * @return name
     */
    default String getServiceName(Class<?> serviceClazz) {
        return serviceClazz.getName();
    }
}
