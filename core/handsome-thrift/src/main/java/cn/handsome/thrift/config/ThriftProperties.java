package cn.handsome.thrift.config;

import cn.handsome.core.micro.ServiceAddress;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * thrift 配置
 *
 * @author shay
 * @date 2021/5/28
 **/
@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "handsome.thrift")
public class ThriftProperties {
    @NestedConfigurationProperty
    private ServiceAddress server;

    @NestedConfigurationProperty
    private Map<String, List<ServiceAddress>> clients;

    ThriftProperties() {
        server = new ServiceAddress("localhost", 8090);
        clients = new HashMap<>();
    }
}
