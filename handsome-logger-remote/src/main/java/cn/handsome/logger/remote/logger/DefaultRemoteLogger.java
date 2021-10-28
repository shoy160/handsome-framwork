package cn.handsome.logger.remote.logger;

import cn.handsome.logger.remote.RemoteLogger;
import cn.handsome.logger.remote.RemoteLoggerManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.event.Level;

/**
 * @author shay
 * @date 2021/4/7
 */
@RequiredArgsConstructor
public class DefaultRemoteLogger implements RemoteLogger {
    private final RemoteLoggerManager socketManager;

    @Override
    public boolean isEnabled(Level level) {
        return socketManager.isEnabled(level);
    }

    @Override
    public void log(Level level, Object msg) {
        socketManager.send(level, msg, false);
    }
}
