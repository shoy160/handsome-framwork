package cn.handsome.core.utils;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shoy
 * @date 2021/7/8
 */
public final class HtmlUtil {
    private final static String HTML5_TEMPLATE = "<!DOCTYPE html><html lang=\"zh\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">%s</head><body>%s</body></html>";
    private final static String HTML5_BASE_CSS = "<style>body{margin: 0;}img{max-width: 100% !important;}</style>";
    private final static String CSS_LINK_TEMPLATE = "<link rel=\"stylesheet\" href=\"%s\">";
    private final static String BASE_CSS = "https://file.handsome.cn/fed/css/h5_base.css";
    private final static Pattern REG_HTML5 = Pattern.compile("meta\\s+name=\"viewport\"", Pattern.DOTALL);

    /**
     * 完善的H5规范
     *
     * @param content 富文本内容
     * @return html5
     */
    public static String prettyH5(String content) {
        return prettyH5(content, BASE_CSS);
    }

    /**
     * 完善的H5规范
     *
     * @param content 富文本内容
     * @param cssUrl  css
     * @return html5
     */
    public static String prettyH5(String content, String cssUrl) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        Matcher matcher = REG_HTML5.matcher(content);
        if (!matcher.find()) {
            StringBuilder buffer = new StringBuilder(HTML5_BASE_CSS);
            if (StrUtil.isNotBlank(cssUrl)) {
                buffer.append(String.format(CSS_LINK_TEMPLATE, cssUrl));
            }
            return String.format(HTML5_TEMPLATE, buffer, content);
        }
        return content;
    }
}
