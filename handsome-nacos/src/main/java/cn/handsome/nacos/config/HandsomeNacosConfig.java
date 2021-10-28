package cn.handsome.nacos.config;

import cn.handsome.nacos.route.NacosRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author shoy
 * @date 2021/6/25
 */
@Slf4j
@Configuration
public class HandsomeNacosConfig {

    @Bean
    @Primary
    public NacosRouter nacosRouter(NacosProperties config) {
        return new NacosRouter(config);
    }
}
