package cn.handsome.thrift.config;

import cn.handsome.core.micro.route.RouterFinder;
import cn.handsome.core.micro.route.RouterRegister;
import cn.handsome.thrift.client.ThriftClientFactory;
import cn.handsome.thrift.client.impl.DefaultClientFactory;
import cn.handsome.thrift.server.DescriptorFinder;
import cn.handsome.thrift.server.ThriftServer;
import cn.handsome.thrift.server.impl.DefaultDescriptorFinder;
import cn.handsome.thrift.server.impl.DefaultThriftServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Thrift 自动注入
 *
 * @author shay
 * @date 2021/5/31
 **/
@Component
public class ThriftAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DescriptorFinder processorFinder(ApplicationContext context) {
        return new DefaultDescriptorFinder(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThriftServer thriftServer(ThriftProperties config, DescriptorFinder finder, RouterRegister register) {
        return new DefaultThriftServer(config, finder, register);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThriftClientFactory thriftClientFactory(RouterFinder finder) {
        return new DefaultClientFactory(finder);
    }
}
