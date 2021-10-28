package cn.handsome.core.gcode;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author shoy
 * @date 2021/6/30
 */
public final class CodeHelper {
    private final static String BASE_NUMBERS = "0123456789";
    private final static String BASE_LETTER = "abcdefghijklmnopqrstuvwxyz";
    private final static Pattern REG_MONTH_FORMAT = Pattern.compile("^y+M+$", Pattern.DOTALL);
    private final static Pattern REG_YEAR_FORMAT = Pattern.compile("^y+$", Pattern.DOTALL);

    public static Date expire(String format) {
        Date expire = DateUtil.beginOfDay(DateUtil.tomorrow());
        if (StrUtil.isNotBlank(format)) {
            if (REG_MONTH_FORMAT.matcher(format).find()) {
                expire = DateUtil.beginOfMonth(DateUtil.nextMonth());
            } else if (REG_YEAR_FORMAT.matcher(format).find()) {
                expire = DateUtil.beginOfYear(DateUtil.date().offset(DateField.YEAR, 1));
            }
        }
        return expire;
    }

    public static String random(CodeType type, int length) {
        return random(new CodeRule(length, type));
    }

    public static String random(CodeRule rule) {
        CodeType type = rule.getType();
        if (type == CodeType.DateFormat) {
            String format = rule.getFormat();
            if (StrUtil.isBlank(format)) {
                format = "yyyyMMdd";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String date = dateFormat.format(new Date());
            return date.concat(random(CodeType.Number, rule.getLength()));
        }
        String baseString;
        switch (type) {
            case Letter:
                baseString = BASE_LETTER;
                break;
            case UpperLetter:
                baseString = BASE_LETTER.toUpperCase();
                break;
            case NumberAndLetter:
                baseString = BASE_NUMBERS.concat(BASE_LETTER);
                break;
            case NumberAndUpperLetter:
                baseString = BASE_NUMBERS.concat(BASE_LETTER.toUpperCase());
                break;
            case NumberAndAllLetter:
                baseString = BASE_NUMBERS.concat(BASE_LETTER).concat(BASE_LETTER.toUpperCase());
                break;
            default:
                baseString = BASE_NUMBERS;
                break;
        }
        return RandomUtil.randomString(baseString, rule.getLength());
    }
}
