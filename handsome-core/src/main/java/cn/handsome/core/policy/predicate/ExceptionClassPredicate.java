package cn.handsome.core.policy.predicate;

import cn.handsome.core.policy.attempt.Attempt;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@RequiredArgsConstructor
public class ExceptionClassPredicate<V> implements Predicate<Attempt<V>> {
    private final Class<? extends Throwable> exceptionClass;

    @Override
    public boolean test(Attempt<V> attempt) {
        return attempt.hasException()
                && this.exceptionClass.isAssignableFrom(attempt.getExceptionCause().getClass());
    }
}
