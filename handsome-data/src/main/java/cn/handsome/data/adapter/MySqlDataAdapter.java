package cn.handsome.data.adapter;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.data.DataAdapter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;

/**
 * @author luoyong
 * @date 2023/12/28
 */
public class MySqlDataAdapter implements DataAdapter {
    @Override
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    @Override
    public <T> T jsonContains(Join<T> join, String column, Object value) {
        return join.apply(String.format("json_contains(%s,{0})", column), JsonUtils.toJson(value));
    }
}
