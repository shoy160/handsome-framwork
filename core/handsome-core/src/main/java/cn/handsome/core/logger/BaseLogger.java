package cn.handsome.core.logger;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.core.utils.TypeUtils;
import org.slf4j.event.Level;

/**
 * @author luoyong
 * @date 2025/3/10
 */
public abstract class BaseLogger implements Logger {
    private final LoggerHandler[] handlers;

    protected BaseLogger(LoggerHandler[] handlers) {
        this.handlers = handlers;
    }


    protected LogMessage getMessage(Level level, Object msg) {
        LogMessage message = new LogMessage();
        if (CommonUtils.isNotEmpty(msg)) {
            if (TypeUtils.isSimple(msg.getClass())) {
                message.put("message", msg);
            } else {
                message.putAll(MapUtils.map(msg));
            }
        }
        message.put("level", level);
        message.resolve(this.handlers);
        return message;
    }
}
