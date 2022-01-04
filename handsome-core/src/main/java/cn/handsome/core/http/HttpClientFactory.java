package cn.handsome.core.http;

import cn.handsome.core.Singleton;
import cn.handsome.core.http.handler.ClientProxyHandler;
import cn.handsome.core.proxy.ProxyFactory;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;

/**
 * @author shay
 * @date 2021/3/18
 */
public interface HttpClientFactory extends ProxyFactory {

    /**
     * 创建代理
     *
     * @param clazz clazz
     * @return instance
     */
    default Object create(Class<?> clazz) {
        return create(clazz, new ClientProxyHandler(-1, null));
    }

    /**
     * 创建代理
     *
     * @param clazz  clazz
     * @param filter filter
     * @return instance
     */
    default Object create(Class<?> clazz, HttpClientFilter filter) {
        return create(clazz, new ClientProxyHandler(-1, filter));
    }

    /**
     * 创建代理
     *
     * @param clazz class
     * @param <T>   T
     * @return T
     */
    default <T> T createT(Class<T> clazz) {
        return createT(clazz, new ClientProxyHandler(-1, null));
    }

    /**
     * 创建代理
     *
     * @param clazz  class
     * @param filter filter
     * @param <T>    T
     * @return T
     */
    default <T> T createT(Class<T> clazz, HttpClientFilter filter) {
        return createT(clazz, new ClientProxyHandler(-1, filter));
    }


    /**
     * 创建代理
     *
     * @param clazz   class
     * @param timeout 超时时间(秒)
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, Integer timeout) {
        return createT(clazz, new ClientProxyHandler(timeout, null));
    }

    /**
     * 创建代理
     *
     * @param clazz   class
     * @param timeout 超时时间(秒)
     * @param filter  filter
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, Integer timeout, HttpClientFilter filter) {
        return createT(clazz, new ClientProxyHandler(timeout, filter));
    }

    /**
     * 获取默认实例
     *
     * @return instance
     */
    static HttpClientFactory instance() {
        return Singleton.instance(new HttpClientFactory() {
            @Override
            public Object create(Class<?> clazz, InvocationHandler handler) {
                return ProxyFactory.instance().create(clazz, handler);
            }

            @Override
            public Object create(Class<?> clazz, MethodInterceptor interceptor) {
                return ProxyFactory.instance().create(clazz, interceptor);
            }
        });
    }
}
