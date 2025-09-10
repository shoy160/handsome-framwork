package cn.handsome.data.handler;

/**
 * @author shoy
 * @date 2021/6/30
 */
public class SeparatorLongHandler extends SeparatorTypeHandler<Long> {
    public SeparatorLongHandler() {
        super(Long.class);
    }

    @Override
    public Long parse(String value) {
        return Long.getLong(value);
    }

    @Override
    public String toString(Long value) {
        return String.valueOf(value);
    }
}
