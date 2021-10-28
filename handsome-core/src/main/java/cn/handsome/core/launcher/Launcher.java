package cn.handsome.core.launcher;

/**
 * 应用加载接口
 *
 * @author shay
 * @date 2020/8/14
 */
public interface Launcher {
    /**
     * 预加载
     */
    default void preLoad() {
    }

    /**
     * 加载
     */
    default void onLoad() {
    }

    /**
     * 销毁
     */
    default void onDestroy() {

    }
}
