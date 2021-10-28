package cn.handsome.thrift.client.impl;

import cn.handsome.core.enums.ResultCode;
import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.micro.route.RouterFinder;
import cn.handsome.core.utils.ArrayUtils;
import cn.handsome.thrift.client.ThriftClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.async.TAsyncClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * todo
 *
 * @author shay
 * @date 2021/5/28
 **/
@Slf4j
@RequiredArgsConstructor
public class DefaultClientFactory implements ThriftClientFactory {
    private final RouterFinder finder;

    private TSocket createSocket(Class<?> clazz) {
        //服务发现
        List<ServiceAddress> addressList = finder.find(clazz);
        if (null == addressList || addressList.size() == 0) {
            return null;
        }
        while (addressList.size() > 0) {
            ServiceAddress address = ArrayUtils.weightRandom(addressList);
            if (null == address) {
                addressList.remove(null);
                continue;
            }
            TSocket socket;
            try {
                socket = new TSocket(address.getService(), address.getServicePort());
                //连接超时时间200ms
                socket.setConnectTimeout(120);
                socket.open();
                return socket;
            } catch (TTransportException e) {
                log.warn("Thrift Client create error:{}", e.getLocalizedMessage());
                addressList.remove(address);
            }
        }
        return null;
    }

    @Override
    public Object createClient(Class<?> clazz) {
        //服务发现
        TSocket socket = createSocket(clazz);
        if (null == socket) {
            throw new BusinessException(ResultCode.NO_SERVICE);
        }
        TBinaryProtocol protocol = new TBinaryProtocol(socket);
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, clazz.getName());
        try {
            return clazz
                    .getConstructor(TProtocol.class)
                    .newInstance(multiplexedProtocol);
        } catch (Exception e) {
            log.warn("创建Thrift客户端失败", e);
            throw new BusinessException(ResultCode.NO_SERVICE);
        }
    }

    @Override
    public <T extends TAsyncClient> T createAsync(Class<T> clazz) {
        throw new BusinessException("NotImplemented");
    }
}
