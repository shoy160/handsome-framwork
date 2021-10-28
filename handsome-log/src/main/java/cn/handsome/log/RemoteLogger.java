package cn.handsome.log;


import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.core.utils.TypeUtils;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 远程日志
 *
 * @author shay
 * @date 2021/4/2
 */
public interface RemoteLogger {

    /**
     * 是否开启日志等级
     *
     * @param level level
     * @return boolean
     */
    boolean isEnabled(Level level);

    /**
     * 记录日志
     *
     * @param level level
     * @param msg   消息体
     */
    void log(Level level, Object msg);

    /**
     * trace
     *
     * @param msg msg
     */
    default void trace(Object msg) {
        log(Level.TRACE, msg);
    }

    /**
     * 是否开启Trace
     *
     * @return boolean
     */
    default boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    /**
     * debug
     *
     * @param msg msg
     */
    default void debug(Object msg) {
        log(Level.DEBUG, msg);
    }

    /**
     * 是否开启Debug
     *
     * @return boolean
     */
    default boolean isDebugEnabled() {
        return isEnabled(Level.DEBUG);
    }

    /**
     * info
     *
     * @param msg msg
     */
    default void info(Object msg) {
        log(Level.INFO, msg);
    }

    /**
     * 是否开启Info
     *
     * @return boolean
     */
    default boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    /**
     * warn
     *
     * @param msg msg
     */
    default void warn(Object msg) {
        log(Level.WARN, msg);
    }

    /**
     * 是否开启Warn
     *
     * @return boolean
     */
    default boolean isWarnEnabled() {
        return isEnabled(Level.WARN);
    }

    /**
     * error
     *
     * @param msg msg
     */
    default void error(Object msg) {
        log(Level.ERROR, msg);
    }

    /**
     * error
     *
     * @param msg       msg
     * @param throwable throwable
     */
    default void error(Object msg, Throwable throwable) {
        Map<String, Object> msgMap = new HashMap<>();
        if (CommonUtils.isNotEmpty(msg)) {
            if (TypeUtils.isSimple(msg.getClass())) {
                msgMap.put("message", msg);
            } else {
                msgMap = MapUtils.map(msg);
            }
        }
        if (throwable != null) {
            try (StringWriter out = new StringWriter()) {
                try (PrintWriter writer = new PrintWriter(out)) {
                    throwable.printStackTrace(writer);
                    msgMap.put("exception", out.toString());
                }
            } catch (IOException ignored) {
            }
        }
        log(Level.ERROR, msgMap);
    }

    /**
     * 是否开启Error
     *
     * @return boolean
     */
    default boolean isErrorEnabled() {
        return isEnabled(Level.ERROR);
    }
}
