package cn.handsome.core.observer;

import java.util.Objects;

/**
 * 单次观察者
 *
 * @author shoy
 * @date 2021/12/27
 */
public interface OnceObserver extends Observer {
    /**
     * 定点值
     *
     * @return value
     */
    Object getValue();

    /**
     * 更新
     */
    void update();

    /**
     * 更新
     *
     * @param value   value
     * @param factory factory
     */
    @Override
    default void update(Object value, ObserverFactory factory) {
        if (Objects.equals(value, getValue())) {
            update();
        }
    }
}
