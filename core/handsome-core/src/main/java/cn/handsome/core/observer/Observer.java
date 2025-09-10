package cn.handsome.core.observer;

/**
 * 观察者
 *
 * @author shoy
 * @date 2021/12/27
 */
public interface Observer {

    /**
     * 更新
     *
     * @param arg     arg
     * @param factory factory
     */
    void update(Object arg, ObserverFactory factory);
}
