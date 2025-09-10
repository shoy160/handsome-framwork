package cn.handsome.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;

/**
 * @author shoy
 * @date 2021/6/10
 */
public interface ThriftFunc<R, T extends TServiceClient> {
    /**
     * Thrift Func
     *
     * @param client client
     * @return result
     * @throws TException ex
     */
    R invoke(T client) throws TException;
}
