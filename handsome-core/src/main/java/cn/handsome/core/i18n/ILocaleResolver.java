package cn.handsome.core.i18n;

import java.util.Locale;
import java.util.Optional;

/**
 * @author luoyong
 * @date 2024/6/21
 */
public interface ILocaleResolver {
    String getLanguage();

    default Locale getLocale() {
        String language = getLanguage();
        Locale locale = R.parseLanguage(language);
        return Optional.ofNullable(locale).orElse(Locale.getDefault());
    }
}
