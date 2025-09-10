package cn.handsome.logger.remote.logger;

import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.logger.remote.config.RemoteLoggerProperties;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author luoyong
 * @date 2025/3/10
 */
@RequiredArgsConstructor
public class RemoteLoggerHandler implements LoggerHandler {
    private final RemoteLoggerProperties config;

    @Override
    public void render(Map<String, Object> message) {
        if (CommonUtils.isNotEmpty(config.getProject())) {
            message.put("project", config.getProject());
        }
        if (CommonUtils.isNotEmpty(config.getAppName())) {
            message.put("app", config.getAppName());
        }
    }
}
