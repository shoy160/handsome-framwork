package cn.handsome.thrift.client;

import cn.handsome.core.lang.Action;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

/**
 * client factory
 *
 * @author shay
 * @date 2021/5/28
 **/
public interface ThriftClientFactory {
    /**
     * 创建客户端
     *
     * @param <T>   client
     * @param clazz class
     * @return client
     */
    default <T extends TServiceClient> T create(Class<T> clazz) {
        Object instance = createClient(clazz);
        return clazz.cast(instance);
    }

    /**
     * 创建客户端
     *
     * @param clazz class
     * @return client instance
     */
    Object createClient(Class<?> clazz);

    /**
     * 创建客户端
     *
     * @param <T>   client
     * @param clazz class
     * @return client
     */
    <T extends TAsyncClient> T createAsync(Class<T> clazz);

    /**
     * 使用客户端(使用完自动关闭)
     *
     * @param clazz  class
     * @param action action
     * @param <T>    client type
     */
    default <T extends TServiceClient> void use(Class<T> clazz, Action<T> action) {
        T client = null;
        try {
            client = create(clazz);
            if (client != null) {
                action.invoke(client);
            }
        } finally {
            close(client);
        }
    }

    /**
     * Thrift 调用
     *
     * @param clientClass clientClass
     * @param func        func
     * @param <T>         TClient
     * @param <R>         TResult
     * @return result
     * @throws TException ex
     */
    default <T extends TServiceClient, R> R call(Class<T> clientClass, ThriftFunc<R, T> func) throws TException {
        T client = null;
        try {
            client = create(clientClass);
            if (client != null) {
                return func.invoke(client);
            }
            throw new TTransportException("RPC客户端创建失败");
        } catch (TException e) {
            if (e instanceof TApplicationException
                    && ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
                return null;
            }
            throw e;
        } finally {
            close(client);
        }
    }

    /**
     * 关闭客户端
     *
     * @param client client
     * @param <T>    TServiceClient
     */
    default <T extends TServiceClient> void close(T client) {
        if (client != null) {
            TProtocol protocol = client.getInputProtocol();
            if (protocol != null && protocol.getTransport().isOpen()) {
                protocol.getTransport().close();
            }
            protocol = client.getOutputProtocol();
            if (protocol != null && protocol.getTransport().isOpen()) {
                protocol.getTransport().close();
            }
        }
    }
}
