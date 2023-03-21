package cn.handsome.web.config;

import cn.handsome.core.AppContext;
import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.web.HandsomeApplication;
import cn.handsome.web.security.AuthContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author shoy
 * @date 2021/10/28
 */
@Component
public class ServletLoggerHandler implements LoggerHandler {
    private final static String[] TOKEN_HEADERS = new String[]{"Token", "Jwt-Token", "Authorization"};

    @Override
    public void complete(Map<String, Object> message) {
        if (null == message) {
            return;
        }
        message.putIfAbsent("app", AppContext.getAppName());
        message.putIfAbsent("mode", AppContext.getAppMode());
        HttpServletRequest request = AuthContext.getRequest();
        if (request != null) {
            message.putIfAbsent("http-method", request.getMethod());
            message.putIfAbsent("url", AuthContext.currentUrl());
            message.putIfAbsent("client-ip", AuthContext.getClientIp());
            message.putIfAbsent("user-agent", request.getHeader("User-Agent"));
            String referer = request.getHeader("referer");
            if (CommonUtils.isNotEmpty(referer)) {
                message.putIfAbsent("referer", referer);
            }
            byte[] body = AuthContext.getBody();
            if (CommonUtils.isNotEmpty(body)) {
                String data = new String(body, Charset.defaultCharset());
                message.putIfAbsent("data", data);
            }
            for (String key : TOKEN_HEADERS) {
                String value = request.getHeader(key);
                if (CommonUtils.isNotEmpty(value)) {
                    message.putIfAbsent("token", value);
                    break;
                }
            }

        }
    }
}
