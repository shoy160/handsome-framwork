package cn.handsome.data.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分隔符类型处理器
 *
 * @author shoy
 * @date 2021/6/30
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SeparatorTypeHandler<T> extends BaseTypeHandler<List<T>> {
    private final String separator;
    private final Class<T> type;

    public SeparatorTypeHandler(Class<T> type) {
        this(",", type);
    }

    public SeparatorTypeHandler(String separator, Class<T> type) {
        if (type == null) {
            throw new NullPointerException("Type argument cannot be null");
        }
        this.separator = separator;
        this.type = type;
    }

    /**
     * 类型转换
     *
     * @param value value
     * @return T
     */
    public T parse(String value) {
        return Convert.convert(type, value);
    }

    /**
     * 转换字符串
     *
     * @param value value
     * @return String
     */
    public String toString(T value) {
        return value.toString();
    }

    private List<T> parseList(String value) {
        if (StrUtil.isNotBlank(value)) {
            return Arrays.stream(value.split(this.separator))
                    .map(this::parse)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<T> ts, JdbcType jdbcType) throws SQLException {
        List<String> stringList = ts.stream().map(this::toString).collect(Collectors.toList());
        String param = String.join(this.separator, stringList);
        preparedStatement.setString(i, param);
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = resultSet.getString(columnName);
        return parseList(value);
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String value = resultSet.getString(columnIndex);
        return parseList(value);
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String value = callableStatement.getString(columnIndex);
        return parseList(value);
    }
}
