package cn.handsome.web.launcher;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.log.RemoteLoggerManager;
import cn.handsome.web.HandsomeApplication;
import cn.handsome.web.security.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

/**
 * @author shay
 * @date 2021/4/9
 */
@Slf4j
@Component
public class RemoteLoggerLauncher implements ApplicationRunner {
    private RemoteLoggerManager socketManager;
    private final static String[] TOKEN_HEADERS = new String[]{"Token", "Jwt-Token", "Authorization"};

    @Autowired(required = false)
    public void setSocketManager(RemoteLoggerManager socketManager) {
        this.socketManager = socketManager;
    }

    private void setMessageBuilder() {
        if (this.socketManager == null) {
            return;
        }
        log.info("set web handler for remoteLogger");
        this.socketManager.setMessageHandler(msg -> {
            HandsomeApplication current = HandsomeApplication.getCurrent();
            if (current != null) {
                msg.putIfAbsent("app", current.getAppName());
                msg.putIfAbsent("mode", HandsomeApplication.getMode());
            }
            HttpServletRequest request = AuthContext.getRequest();
            if (request != null) {
                msg.putIfAbsent("http-method", request.getMethod());
                msg.putIfAbsent("url", AuthContext.currentUrl());
                msg.putIfAbsent("client-ip", AuthContext.getClientIp());
                msg.putIfAbsent("user-agent", request.getHeader("User-Agent"));
                String referer = request.getHeader("referer");
                if (CommonUtils.isNotEmpty(referer)) {
                    msg.putIfAbsent("referer", referer);
                }
                byte[] body = AuthContext.getBody();
                if (CommonUtils.isNotEmpty(body)) {
                    String data = new String(body, Charset.defaultCharset());
                    msg.putIfAbsent("data", data);
                }
                for (String key : TOKEN_HEADERS) {
                    String value = request.getHeader(key);
                    if (CommonUtils.isNotEmpty(value)) {
                        msg.putIfAbsent("token", value);
                        break;
                    }
                }

            }
        });
    }

    @Override
    public void run(ApplicationArguments args) {
        setMessageBuilder();
    }
}
