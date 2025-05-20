package cn.handsome.data.handler;

import cn.handsome.core.Constants;
import cn.handsome.core.utils.JsonUtils;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shoy
 * @date 2021/7/1
 */
@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonListTypeHandler<T> extends AbstractJsonTypeHandler<List<T>> {
    private final Class<T> type;

    public JsonListTypeHandler(Class<T> type) {
        super(type);
        this.type = type;
    }

    @Override
    public List<T> parse(String json) {
        return StrUtil.isBlank(json) ? new ArrayList<>() : JsonUtils.jsonList(json, type);
    }

    @Override
    public String toJson(List<T> obj) {
        return null == obj ? Constants.STR_EMPTY : JsonUtils.toJson(obj);
    }
}
