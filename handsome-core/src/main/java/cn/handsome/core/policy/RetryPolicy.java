package cn.handsome.core.policy;

import cn.handsome.core.policy.attempt.Attempt;
import cn.handsome.core.policy.attempt.ExceptionAttempt;
import cn.handsome.core.policy.attempt.ResultAttempt;
import cn.handsome.core.policy.strategy.AttemptTimeLimiter;
import cn.handsome.core.policy.strategy.BlockStrategy;
import cn.handsome.core.policy.strategy.StopStrategy;
import cn.handsome.core.policy.strategy.WaitStrategy;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 重试策略
 *
 * @author luoyong
 * @date 2023/2/8
 */
@RequiredArgsConstructor
public class RetryPolicy<T> {
    private final StopStrategy stopStrategy;
    private final WaitStrategy waitStrategy;
    private final BlockStrategy blockStrategy;
    private final AttemptTimeLimiter<T> attemptTimeLimiter;
    private final Predicate<Attempt<T>> rejectionPredicate;
    private final Collection<RetryListener> listeners;

    public T call(Callable<T> callable) throws ExecutionException, RetryException {
        long startTime = System.nanoTime();
        int attemptNumber = 1;

        while (true) {
            Attempt<T> attempt;
            try {
                T result = this.attemptTimeLimiter.call(callable);
                attempt = new ResultAttempt<>(result, attemptNumber, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
            } catch (Throwable ex) {
                attempt = new ExceptionAttempt<>(ex, attemptNumber, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
            }
            for (RetryListener listener : this.listeners) {
                listener.onRetry((Attempt<?>) attempt);
            }

            if (!this.rejectionPredicate.test(attempt)) {
                return attempt.get();
            }

            if (this.stopStrategy.shouldStop(attempt)) {
                throw new RetryException(attemptNumber, attempt);
            }

            long sleepTime = this.waitStrategy.computeSleepTime(attempt);

            try {
                this.blockStrategy.block(sleepTime);
            } catch (InterruptedException var10) {
                Thread.currentThread().interrupt();
                throw new RetryException(attemptNumber, attempt);
            }
            ++attemptNumber;
        }
    }

    public RetryCallable<T> wrap(Callable<T> callable) {
        return new RetryCallable<>(this, callable);
    }
}
