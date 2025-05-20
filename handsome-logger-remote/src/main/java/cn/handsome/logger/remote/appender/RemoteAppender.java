package cn.handsome.logger.remote.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import cn.handsome.core.logger.LogMessage;
import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.logger.remote.RemoteLoggerManager;
import cn.hutool.core.util.ArrayUtil;
import org.slf4j.event.Level;

/**
 * @author shay
 * @date 2021/4/9
 */
public class RemoteAppender extends AppenderBase<ILoggingEvent> {
    private final RemoteLoggerManager socketManager;
    private final LoggerHandler[] loggerHandlers;
    private final static String[] EXCLUDE_LOGGERS = new String[]{
            "cn.handsome.web.GlobalExceptionResolver",
            "cn.handsome.log.RemoteLoggerManager"
    };

    public RemoteAppender(RemoteLoggerManager socketManager, LoggerHandler[] loggerHandlers) {
        this.socketManager = socketManager;
        this.loggerHandlers = loggerHandlers;
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
        LogMessage message = new LogMessage();
        message.put("message", event.getFormattedMessage());
        message.put("logger", event.getLoggerName());
        message.put("level", event.getLevel().toString());
        IThrowableProxy proxy = event.getThrowableProxy();
        if (proxy != null) {
            message.put("exception", formatThrowable(proxy));
        }
        message.resolve(this.loggerHandlers);
        socketManager.send(level, JsonUtils.toJson(message));
    }
}
