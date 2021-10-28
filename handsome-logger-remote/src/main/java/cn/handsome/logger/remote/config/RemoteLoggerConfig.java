package cn.handsome.logger.remote.config;

import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.logger.remote.RemoteLogger;
import cn.handsome.logger.remote.RemoteLoggerManager;
import cn.handsome.logger.remote.logger.DefaultRemoteLogger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private LoggerHandler[] handlers;

    @Autowired(required = false)
    public void setHandlers(LoggerHandler[] handlers) {
        this.handlers = handlers;
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    public RemoteLoggerManager loggerManager(RemoteLoggerProperties config) {
        return new RemoteLoggerManager(config, this.handlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteLogger remoteLogger(RemoteLoggerManager socketManager) {
        return new DefaultRemoteLogger(socketManager);
    }
}
