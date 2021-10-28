package cn.handsome.data.handler;

import cn.handsome.core.enums.BaseEnum;
import cn.handsome.core.enums.BaseNamedEnum;
import cn.handsome.core.enums.ValueEnum;
import cn.handsome.core.utils.EnumUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author shoy
 * @date 2021/6/30
 */
@MappedTypes({Enum.class, BaseEnum.class, BaseNamedEnum.class})
@MappedJdbcTypes({JdbcType.TINYINT, JdbcType.INTEGER})
public class EnumTypeHandler<T extends ValueEnum<Integer>> extends BaseTypeHandler<T> {
    private final Class<T> type;

    public EnumTypeHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, t.getValue());
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Integer value = resultSet.getInt(s);
        return EnumUtils.getEnum(value, type);
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Integer value = resultSet.getInt(i);
        return EnumUtils.getEnum(value, type);
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Integer value = callableStatement.getInt(i);
        return EnumUtils.getEnum(value, type);
    }
}
