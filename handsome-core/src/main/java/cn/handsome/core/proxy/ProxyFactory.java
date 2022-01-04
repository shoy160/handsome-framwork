package cn.handsome.core.proxy;

import cn.handsome.core.Singleton;
import cn.handsome.core.proxy.impl.ProxyFactoryImpl;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;

/**
 * @author shay
 * @date 2021/3/22
 */
public interface ProxyFactory {
    /**
     * 创建代理(JDK)
     *
     * @param clazz   类型
     * @param handler handler
     * @return 实例
     */
    Object create(Class<?> clazz, InvocationHandler handler);

    /**
     * 创建代理(CGlib)
     *
     * @param clazz       clazz
     * @param interceptor interceptor
     * @return instance
     */
    Object create(Class<?> clazz, MethodInterceptor interceptor);

    /**
     * 创建代理(JDK)
     *
     * @param clazz   class
     * @param handler handler
     * @param <T>     T
     * @return T
     */
    default <T> T createT(Class<T> clazz, InvocationHandler handler) {
        Object instance = create(clazz, handler);
        if (instance == null) {
            return null;
        }
        return clazz.cast(instance);
    }

    /**
     * 创建代理(CGlib)
     *
     * @param clazz       class
     * @param interceptor interceptor
     * @param <T>         T
     * @return T
     */
    default <T> T createT(Class<T> clazz, MethodInterceptor interceptor) {
        Object instance = create(clazz, interceptor);
        if (instance == null) {
            return null;
        }
        return clazz.cast(instance);
    }

    /**
     * 获取默认实例
     *
     * @return proxyFactory
     */
    static ProxyFactory instance() {
        return Singleton.instance(ProxyFactoryImpl.class);
    }
}
