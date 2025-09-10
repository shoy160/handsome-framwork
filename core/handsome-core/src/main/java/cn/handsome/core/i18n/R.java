package cn.handsome.core.i18n;

import cn.handsome.core.AppContext;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2024/6/20
 */
@Slf4j
public final class R {
    private final static MessageResource messageResource = new MessageResource();

    public static Locale parseLanguage(String language) {
        String[] array;
        if (StrUtil.isBlank(language) || (array = language.split("[-_]")).length != 2) {
            return null;
        }
        return new Locale(array[0], array[1]);
    }

    public static String getLanguage() {
        Locale locale = getLocale();
        return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }

    public static Locale getLocale() {
        return getLocale(null);
    }

    public static Locale getLocale(String language) {
        try {
            Locale locale = parseLanguage(language);
            if (Objects.nonNull(locale)) {
                return locale;
            }
            ILocaleResolver resolver = AppContext.getBean(ILocaleResolver.class);
            if (Objects.nonNull(resolver)) {
                return resolver.getLocale();
            }
        } catch (Exception ignored) {
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
        Locale locale = getLocale(language);
        log.info("当前语言为：{}", locale.getDisplayName());
        return messageResource.getMessage(code, args, locale, def);
    }
}
