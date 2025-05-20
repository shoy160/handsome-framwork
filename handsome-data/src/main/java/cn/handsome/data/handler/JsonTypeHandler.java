package cn.handsome.data.handler;

import cn.handsome.core.utils.JsonUtils;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 * Json 类型处理器
 *
 * @author shoy
 * @date 2021/6/30
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonTypeHandler<T> extends AbstractJsonTypeHandler<T> {
    private final Class<T> type;

    public JsonTypeHandler(Class<T> type) {
        super(type);
        if (type == null) {
            throw new NullPointerException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public T parse(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JsonUtils.json(json, this.type);
    }

    @Override
    public String toJson(T obj) {
        if (null == obj) {
            return null;
        }
        return JsonUtils.toJson(obj);
    }
}
