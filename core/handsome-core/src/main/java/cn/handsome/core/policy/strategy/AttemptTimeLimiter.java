package cn.handsome.core.policy.strategy;

import java.util.concurrent.Callable;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public interface AttemptTimeLimiter<V> {
    V call(Callable<V> var1) throws Exception;
}
