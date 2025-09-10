package cn.handsome.data.adapter;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.data.DataAdapter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;

/**
 * @author luoyong
 * @date 2023/12/28
 */
public class PostgreSqlDataAdapter implements DataAdapter {
    @Override
    public DbType getDbType() {
        return DbType.POSTGRE_SQL;
    }

    @Override
    public <T> T jsonContains(Join<T> join, String column, Object value) {
        return join.apply(String.format("jsonb_contains(%s::jsonb,{0}::jsonb)", column), JsonUtils.toJson(value));
    }
}
