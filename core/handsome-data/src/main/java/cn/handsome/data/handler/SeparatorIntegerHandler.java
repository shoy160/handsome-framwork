package cn.handsome.data.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 * @author shoy
 * @date 2021/6/30
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SeparatorIntegerHandler extends SeparatorTypeHandler<Integer> {
    public SeparatorIntegerHandler() {
        super(Integer.class);
    }

    @Override
    public Integer parse(String value) {
        return Integer.getInteger(value);
    }

    @Override
    public String toString(Integer value) {
        return String.valueOf(value);
    }
}
