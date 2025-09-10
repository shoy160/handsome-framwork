package cn.handsome.core.enums;

/**
 * @author shoy
 * @date 2021/6/8
 */
public enum TimestampType implements BaseEnum {
    /**
     * 不开启时间戳
     */
    None,
    /**
     * 秒
     */
    Second,
    /**
     * 毫秒
     */
    MilliSecond,
    ;

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
