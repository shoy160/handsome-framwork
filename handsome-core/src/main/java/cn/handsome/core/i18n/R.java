package cn.handsome.core.i18n;

import cn.handsome.core.AppContext;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.env.Environment;

import java.util.Locale;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2024/6/20
 */
public final class R {
    private final static MessageResource messageResource;

    static {
        Environment environment = AppContext.getBean(Environment.class);
        messageResource = new MessageResource(environment);
    }

    public static Locale convert(String language) {
        try {
            String[] array;
            if (StrUtil.isNotBlank(language) && (array = language.split("-")).length == 2) {
                return new Locale(array[0], array[1]);
            }
        } catch (Exception ignored) {
        }

        ILocaleResolver resolver = AppContext.getBean(ILocaleResolver.class);
        if (Objects.nonNull(resolver)) {
            return resolver.getLocale();
        }
        return Locale.getDefault();
    }

    public static String message(RCode code) {
        return message(code.name(), null, code.name());
    }

    public static String message(RCode code, Object... args) {
        return message(code.name(), args, code.name());
    }

    public static String message(String code) {
        return message(code, null, code);
    }

    public static String message(String code, Object... args) {
        return message(code, args, code);
    }

    public static String message(String code, String def) {
        return message(code, null, def);
    }

    public static String message(String code, Object[] args, String def) {
        return message(code, args, null, def);
    }

    public static String message(String code, Object[] args, String language, String def) {
        Locale locale = convert(language);
        return messageResource.getMessage(code, args, locale, def);
    }
}
