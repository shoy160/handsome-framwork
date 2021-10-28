package cn.handsome.demo.web.config;

import cn.handsome.demo.client.UserRpcService;
import cn.handsome.thrift.client.ThriftClient;
import cn.handsome.thrift.client.ThriftClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author shoy
 * @date 2021/6/4
 */
@Configuration
public class DemoConfig {

    @Bean
    @RequestScope
    public ThriftClient<UserRpcService.Client> userClient(ThriftClientFactory clientFactory) {
        UserRpcService.Client client = clientFactory.create(UserRpcService.Client.class);
        return new ThriftClient<>(clientFactory, client);
    }

//    @Bean
//    public Cache<String, String> cacheBean(RedisTemplate<String, String> redisTemplate) {
//        return new RedisCache<>(redisTemplate, "framework_demo");
//    }
}
