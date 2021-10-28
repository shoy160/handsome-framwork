package cn.handsome.core.logger;

import java.util.Map;

/**
 * @author shoy
 * @date 2021/10/28
 */
public interface LoggerHandler {
    /**
     * 填充日志消息
     *
     * @param message 消息
     */
    void complete(Map<String, Object> message);
}
