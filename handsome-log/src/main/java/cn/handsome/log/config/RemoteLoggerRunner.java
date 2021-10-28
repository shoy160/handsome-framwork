package cn.handsome.log.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import cn.handsome.log.RemoteLoggerManager;
import cn.handsome.log.appender.RemoteAppender;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author shay
 * @date 2021/4/9
 */
@Slf4j
@Component
public class RemoteLoggerRunner implements ApplicationRunner {
    private RemoteLoggerManager socketManager;

    @Autowired(required = false)
    public void setSocketManager(RemoteLoggerManager socketManager) {
        this.socketManager = socketManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        LoggerContext context = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
        RemoteAppender appender = new RemoteAppender(socketManager);
        appender.setContext(context);
        appender.start();
        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(appender);
        log.info("add remote logger appender");
    }
}
