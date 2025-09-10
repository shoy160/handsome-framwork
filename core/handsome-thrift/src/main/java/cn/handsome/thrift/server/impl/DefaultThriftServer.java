package cn.handsome.thrift.server.impl;

import cn.handsome.core.micro.ServiceAddress;
import cn.handsome.core.micro.route.RouterRegister;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.thrift.config.ThriftProperties;
import cn.handsome.thrift.domain.ThriftDescriptor;
import cn.handsome.thrift.server.DescriptorFinder;
import cn.handsome.thrift.server.ThriftServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * todo
 *
 * @author shay
 * @date 2021/5/28
 **/
@Slf4j
@RequiredArgsConstructor
public class DefaultThriftServer implements ThriftServer {
    private final ThriftProperties config;
    private final DescriptorFinder finder;
    private final RouterRegister register;

    @Override
    public void start() {
        try {
            ServiceAddress address = config.getServer();
            ThriftDescriptor[] descriptorList = finder.find();
            TServerTransport serverTransport = new TServerSocket(address.getPort());
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
            List<Class<?>> classList = new ArrayList<>();
            TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
            for (ThriftDescriptor descriptor : descriptorList) {
                multiplexedProcessor.registerProcessor(descriptor.getServiceName(), descriptor.getProcessor());
                classList.add(descriptor.getClientClass());
            }
            serverArgs.processor(multiplexedProcessor);
            TServer server = new TThreadPoolServer(serverArgs);
            String host = address.getHost();
            log.info("Starting thrift server at {}:{}...", CommonUtils.isEmpty(host) ? "localhost" : host, address.getPort());
            register.register(classList, address);
            server.serve();
        } catch (Exception e) {
            log.error("Thrift服务启动异常", e);
        }
    }
}
