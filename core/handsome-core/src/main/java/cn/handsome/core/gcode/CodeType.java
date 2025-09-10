package cn.handsome.core.gcode;

/**
 * @author shoy
 * @date 2021/6/30
 */
public enum CodeType {
    /**
     * 数字
     */
    Number,
    /**
     * 字母
     */
    Letter,
    /**
     * 大写字母
     */
    UpperLetter,
    /**
     * 数字&字母
     */
    NumberAndLetter,
    /**
     * 数字&大写字母
     */
    NumberAndUpperLetter,
    /**
     * 数字&字母&大写字母
     */
    NumberAndAllLetter,
    /**
     * 日期格式化
     */
    DateFormat
}
