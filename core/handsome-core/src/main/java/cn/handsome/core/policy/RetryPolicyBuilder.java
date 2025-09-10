package cn.handsome.core.policy;

import cn.handsome.core.policy.attempt.Attempt;
import cn.handsome.core.policy.predicate.ExceptionClassPredicate;
import cn.handsome.core.policy.predicate.ExceptionPredicate;
import cn.handsome.core.policy.predicate.Predicates;
import cn.handsome.core.policy.predicate.ResultPredicate;
import cn.handsome.core.policy.strategy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 重试策略构建器
 *
 * @author luoyong
 * @date 2023/2/8
 */
public class RetryPolicyBuilder<V> {
    private AttemptTimeLimiter<V> attemptTimeLimiter;
    private StopStrategy stopStrategy;
    private WaitStrategy waitStrategy;
    private BlockStrategy blockStrategy;
    private Predicate<Attempt<V>> rejectionPredicate = Predicates.alwaysFalse();
    private final List<RetryListener> listeners = new ArrayList<>();

    private RetryPolicyBuilder() {
    }

    public static <V> RetryPolicyBuilder<V> newBuilder() {
        return new RetryPolicyBuilder<>();
    }

    public RetryPolicyBuilder<V> withRetryListener(RetryListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public RetryPolicyBuilder<V> withWaitStrategy(WaitStrategy waitStrategy) throws IllegalStateException {
        this.waitStrategy = waitStrategy;
        return this;
    }

    public RetryPolicyBuilder<V> withStopStrategy(StopStrategy stopStrategy) throws IllegalStateException {
        this.stopStrategy = stopStrategy;
        return this;
    }

    public RetryPolicyBuilder<V> withBlockStrategy(BlockStrategy blockStrategy) throws IllegalStateException {
        this.blockStrategy = blockStrategy;
        return this;
    }

    public RetryPolicyBuilder<V> withAttemptTimeLimiter(AttemptTimeLimiter<V> attemptTimeLimiter) {
        this.attemptTimeLimiter = attemptTimeLimiter;
        return this;
    }

    public RetryPolicyBuilder<V> retryIfException() {
        this.rejectionPredicate = this.rejectionPredicate.or(new ExceptionClassPredicate<>(Exception.class));
        return this;
    }

    public RetryPolicyBuilder<V> retryIfRuntimeException() {
        this.rejectionPredicate = this.rejectionPredicate.or(new ExceptionClassPredicate<>(RuntimeException.class));
        return this;
    }

    public RetryPolicyBuilder<V> retryIfExceptionOfType(Class<? extends Throwable> exceptionClass) {
        this.rejectionPredicate = this.rejectionPredicate.or(new ExceptionClassPredicate<>(exceptionClass));
        return this;
    }

    public RetryPolicyBuilder<V> retryIfException(Predicate<Throwable> exceptionPredicate) {
        this.rejectionPredicate = this.rejectionPredicate.or(new ExceptionPredicate<>(exceptionPredicate));
        return this;
    }

    public RetryPolicyBuilder<V> retryIfResult(Predicate<V> resultPredicate) {
        this.rejectionPredicate = this.rejectionPredicate.or(new ResultPredicate<>(resultPredicate));
        return this;
    }

    public RetryPolicy<V> build() {
        AttemptTimeLimiter<V> theAttemptTimeLimiter = this.attemptTimeLimiter == null ? AttemptTimeLimiters.noTimeLimit() : this.attemptTimeLimiter;
        StopStrategy theStopStrategy = this.stopStrategy == null ? StopStrategies.neverStop() : this.stopStrategy;
        WaitStrategy theWaitStrategy = this.waitStrategy == null ? WaitStrategies.noWait() : this.waitStrategy;
        BlockStrategy theBlockStrategy = this.blockStrategy == null ? BlockStrategies.threadSleepStrategy() : this.blockStrategy;
        return new RetryPolicy<>(theStopStrategy, theWaitStrategy, theBlockStrategy, theAttemptTimeLimiter, this.rejectionPredicate, this.listeners);
    }
}
