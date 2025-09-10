package cn.handsome.core.logger.impl;

import cn.handsome.core.logger.BaseLogger;
import cn.handsome.core.logger.LogMessage;
import cn.handsome.core.logger.LoggerHandler;
import cn.hutool.core.date.DateUtil;
import org.slf4j.event.Level;

import java.util.Date;

/**
 * @author luoyong
 * @date 2025/3/10
 */
public class DefaultLogger extends BaseLogger {

    public DefaultLogger(LoggerHandler[] handlers) {
        super(handlers);
    }

    @Override
    public boolean isEnabled(Level level) {
        return true;
    }

    @Override
    public void log(Level level, Object msg) {
        LogMessage message = getMessage(level, msg);
        System.out.printf("%s [%s] %s %s%n", DateUtil.formatDateTime(new Date()), Thread.currentThread().getName(), message.getLevel(), message.getMessage());
    }
}
