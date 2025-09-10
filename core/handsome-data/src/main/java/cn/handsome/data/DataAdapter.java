package cn.handsome.data;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;

/**
 * @author luoyong
 * @date 2023/12/28
 */
public interface DataAdapter {
    DbType getDbType();

    /**
     * Json 包含
     */
    <T> T jsonContains(Join<T> join, String column, Object value);

    default <T> T limit(Join<T> join, int limit) {
        return join.last(String.format("LIMIT %d", limit));
    }

    default <T> T offset(Join<T> join, int offset) {
        return join.last(String.format("OFFSET %d", offset));
    }

    default <T> T offsetLimit(Join<T> join, int offset, int limit) {
        return join.last(String.format("LIMIT %d OFFSET %d", limit, offset));
    }
}
