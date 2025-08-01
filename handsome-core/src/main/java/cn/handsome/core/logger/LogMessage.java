package cn.handsome.core.logger;

import cn.handsome.core.utils.MapUtils;
import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.event.Level;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author luoyong
 * @date 2025/3/10
 */
@Getter
@NoArgsConstructor
public class LogMessage extends HashMap<String, Object> implements Serializable {
    private Level level;
    private String message;
    private Throwable throwable;

    public LogMessage(Level level, String message, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.throwable = throwable;
    }

    public LogMessage(Level level, String message) {
        this(level, message, null);
    }

    public LogMessage(String message) {
        this(Level.INFO, message, null);
    }

    public LogMessage(Object data) {
        this.putAll(MapUtils.map(data));
    }

    public void setLevel(Level level) {
        this.level = level;
        this.put("level", level);
    }

    public void setMessage(String message) {
        this.message = message;
        this.put("message", message);
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
        this.put("throwable", throwable);
    }

    public void resolve(LoggerHandler[] handlers) {
        if (ArrayUtil.isEmpty(handlers)) {
            return;
        }
        for (LoggerHandler handler : handlers) {
            try {
                handler.render(this);
            } catch (Exception ignored) {
            }
        }
    }

    public static LogMessage debug(String msg) {
        return new LogMessage(Level.DEBUG, msg);
    }

    public static LogMessage info(String msg) {
        return new LogMessage(Level.INFO, msg);
    }

    public static LogMessage warn(String msg, Throwable throwable) {
        return new LogMessage(Level.WARN, msg, throwable);
    }

    public static LogMessage warn(String msg) {
        return new LogMessage(Level.WARN, msg);
    }


    public static LogMessage error(String msg) {
        return new LogMessage(Level.ERROR, msg);
    }

    public static LogMessage error(String msg, Throwable throwable) {
        return new LogMessage(Level.ERROR, msg, throwable);
    }

    public static LogMessage trace(String msg) {
        return new LogMessage(Level.TRACE, msg);
    }
}
