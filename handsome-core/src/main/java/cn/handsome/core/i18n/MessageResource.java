package cn.handsome.core.i18n;

import cn.handsome.core.AppContext;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2024/6/20
 */
public class MessageResource {
    private final ResourceBundleMessageSource messageSource;

    public MessageResource() {
        this(null);
    }

    public MessageResource(String basename) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        if (StrUtil.isBlank(basename)) {
            Environment environment = AppContext.getBean(Environment.class);
            if (Objects.nonNull(environment)) {
                basename = environment.resolvePlaceholders("${spring.messages.basename:i18n/messages}");
            } else {
                basename = "i18n/messages";
            }
        }
        messageSource.setBasename(basename);
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object[] args, Locale locale, String def) {
        try {
            return this.messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            return def;
        }
    }
}
