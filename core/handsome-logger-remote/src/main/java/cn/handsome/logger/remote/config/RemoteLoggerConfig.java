package cn.handsome.logger.remote.config;

import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.logger.remote.RemoteLogger;
import cn.handsome.logger.remote.RemoteLoggerManager;
import cn.handsome.logger.remote.logger.DefaultRemoteLogger;
import cn.handsome.logger.remote.logger.RemoteLoggerHandler;
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
    public RemoteLoggerHandler getRemoteLoggerHandler(RemoteLoggerProperties config) {
        return new RemoteLoggerHandler(config);
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public RemoteLoggerManager loggerManager(RemoteLoggerProperties config) {
        return new RemoteLoggerManager(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteLogger remoteLogger(RemoteLoggerManager socketManager, LoggerHandler[] handlers) {
        return new DefaultRemoteLogger(handlers, socketManager);
    }
}
