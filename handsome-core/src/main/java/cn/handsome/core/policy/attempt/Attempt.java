package cn.handsome.core.policy.attempt;

import java.util.concurrent.ExecutionException;

/**
 * Attempt
 *
 * @author luoyong
 * @date 2023/2/8
 */
public interface Attempt<V> {

    /**
     * 获取结果
     *
     * @return V
     * @throws ExecutionException 执行异常
     */
    V get() throws ExecutionException;

    /**
     * 是否有执行结果
     *
     * @return boolean
     */
    boolean hasResult();

    /**
     * 是否有异常
     *
     * @return boolean
     */
    boolean hasException();

    /**
     * 获取结果
     *
     * @return V
     * @throws IllegalStateException ex
     */
    V getResult() throws IllegalStateException;

    /**
     * 获取异常信息
     *
     * @return throwable
     * @throws IllegalStateException ex
     */
    Throwable getExceptionCause() throws IllegalStateException;

    /**
     * 获取尝试次数
     *
     * @return 尝试次数
     */
    long getAttemptNumber();

    /**
     * 获取执行时间(ms)
     *
     * @return 执行时间(ms)
     */
    long getExecuteTime();
}
