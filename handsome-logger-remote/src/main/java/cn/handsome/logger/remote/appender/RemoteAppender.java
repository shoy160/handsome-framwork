package cn.handsome.logger.remote.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import cn.handsome.logger.remote.RemoteLoggerManager;
import cn.hutool.core.util.ArrayUtil;
import cn.handsome.core.utils.CommonUtils;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shay
 * @date 2021/4/9
 */
public class RemoteAppender extends AppenderBase<ILoggingEvent> {
    private final RemoteLoggerManager socketManager;
    private final static String[] EXCLUDE_LOGGERS = new String[]{
            "cn.handsome.web.GlobalExceptionResolver",
            "cn.handsome.log.RemoteLoggerManager"
    };

    public RemoteAppender(RemoteLoggerManager socketManager) {
        this.socketManager = socketManager;
    }

    private String formatThrowable(IThrowableProxy proxy) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s:%s\r\n", proxy.getClassName(), proxy.getMessage()));
        for (StackTraceElementProxy element : proxy.getStackTraceElementProxyArray()) {
            builder.append(String.format("\tat %s\r\n", element.getSTEAsString()));
        }
        if (CommonUtils.isNotEmpty(proxy.getSuppressed())) {
            for (IThrowableProxy item : proxy.getSuppressed()) {
                builder.append(formatThrowable(item));
            }
        }
        return builder.toString();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (ArrayUtil.contains(EXCLUDE_LOGGERS, event.getLoggerName())) {
            return;
        }
        Level level = Level.valueOf(event.getLevel().toString());
        if (!socketManager.isEnabled(level)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message", event.getFormattedMessage());
        map.put("logger", event.getLoggerName());
        map.put("level", event.getLevel().toString());
        IThrowableProxy proxy = event.getThrowableProxy();
        if (proxy != null) {
            map.put("exception", formatThrowable(proxy));
        }
        socketManager.send(level, map);
    }
}
