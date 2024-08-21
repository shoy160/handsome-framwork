package cn.handsome.web.security;

import cn.handsome.core.i18n.ILocaleResolver;
import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2024/6/20
 */
@Component
public class HandsomeLocaleResolver implements ILocaleResolver {
    @Override
    public String getLanguage() {
        HttpServletRequest request = getRequest();
        if (Objects.isNull(request)) {
            return null;
        }
        String[] keys = {HttpHeaders.ACCEPT_LANGUAGE};
        for (String key : keys) {
            String language = request.getParameter(key);
            if (StrUtil.isNotBlank(language)) {
                return language;
            }
            language = request.getHeader(key);
            if (StrUtil.isNotBlank(language)) {
                return language;
            }
        }
        return null;
    }

    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
