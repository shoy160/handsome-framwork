package cn.handsome.core.policy;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@RequiredArgsConstructor
public class RetryCallable<X> implements Callable<X> {
    private final RetryPolicy<X> retryPolicy;
    private final Callable<X> callable;

    @Override
    public X call() throws ExecutionException, RetryException {
        return this.retryPolicy.call(this.callable);
    }
}
