package cn.handsome.log.config;

import cn.handsome.log.RemoteLogger;
import cn.handsome.log.RemoteLoggerManager;
import cn.handsome.log.logger.DefaultRemoteLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 远程日志
 *
 * @author shay
 * @date 2021/4/7
 */
@Configuration
public class RemoteLoggerConfig {

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public RemoteLoggerManager loggerManager(LoggerProperties config) {
        return new RemoteLoggerManager(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteLogger remoteLogger(RemoteLoggerManager socketManager) {
        return new DefaultRemoteLogger(socketManager);
    }
}
