package cn.handsome.logger.remote.logger;

import cn.handsome.core.logger.BaseLogger;
import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.logger.remote.RemoteLogger;
import cn.handsome.logger.remote.RemoteLoggerManager;
import org.slf4j.event.Level;

import java.util.Map;

/**
 * @author shay
 * @date 2021/4/7
 */
public class DefaultRemoteLogger extends BaseLogger implements RemoteLogger {
    private final RemoteLoggerManager socketManager;

    public DefaultRemoteLogger(LoggerHandler[] handlers, RemoteLoggerManager socketManager) {
        super(handlers);
        this.socketManager = socketManager;
    }

    @Override
    public boolean isEnabled(Level level) {
        return socketManager.isEnabled(level);
    }

    @Override
    public void log(Level level, Object msg) {
        Map<String, Object> message = getMessage(level, msg);
        socketManager.send(level, JsonUtils.toJson(message), false);
    }
}
