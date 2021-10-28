package cn.handsome.core.lang;

/**
 * @author shoy
 * @date 2021/6/30
 */
public interface FuncVoid<T> {
    /**
     * 执行方法
     *
     * @return T
     */
    T invoke();
}
