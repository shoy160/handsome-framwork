package cn.handsome.core.logger;

import cn.handsome.core.utils.MapUtils;
import cn.hutool.core.util.ArrayUtil;
import org.slf4j.event.Level;

import java.util.HashMap;

/**
 * @author luoyong
 * @date 2025/3/10
 */
public class LogMessage extends HashMap<String, Object> {
    public LogMessage() {
    }

    public LogMessage(Object data) {
        this.putAll(MapUtils.map(data));
    }

    public String getMessage() {
        return MapUtils.getValue(this, String.class, "message");
    }

    public void setMessage(String message) {
        this.put("message", message);
    }

    public Level getLevel() {
        return MapUtils.getValue(this, Level.class, "level");
    }

    public void setLevel(Level level) {
        this.put("level", level);
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
}
