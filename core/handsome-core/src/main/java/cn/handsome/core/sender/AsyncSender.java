package cn.handsome.core.sender;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;

/**
 * @author luoyong
 * @date 2023/9/19
 */
public interface AsyncSender<T> {
    /**
     * 推送数据
     *
     * @param data 数据
     */
    void push(T data);

    /**
     * 批量推送数据
     *
     * @param dataList 数据列表
     */
    default void push(Collection<T> dataList) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        dataList.forEach(this::push);
    }
}
