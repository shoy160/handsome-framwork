package cn.handsome.logger.remote.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.logger.remote.RemoteLoggerManager;
import cn.handsome.logger.remote.appender.RemoteAppender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author shay
 * @date 2021/4/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteLoggerRunner implements ApplicationRunner {
    private final RemoteLoggerManager socketManager;
    private final LoggerHandler[] loggerHandlers;


    @Override
    public void run(ApplicationArguments args) {
        // 获取 LoggerContext
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        RemoteAppender appender = new RemoteAppender(socketManager, loggerHandlers);
        appender.setContext(context);
        appender.start();
        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(appender);
        log.info("add remote logger appender");
    }
}
