package cn.handsome.core.policy.predicate;

import cn.handsome.core.policy.attempt.Attempt;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@RequiredArgsConstructor
public class ExceptionPredicate<V> implements Predicate<Attempt<V>> {
    private final Predicate<Throwable> delegate;

    @Override
    public boolean test(Attempt<V> attempt) {
        return attempt.hasException() && this.delegate.test(attempt.getExceptionCause());
    }
}
