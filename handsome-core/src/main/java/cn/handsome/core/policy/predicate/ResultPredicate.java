package cn.handsome.core.policy.predicate;

import cn.handsome.core.policy.attempt.Attempt;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@RequiredArgsConstructor
public class ResultPredicate<V> implements Predicate<Attempt<V>> {
    private final Predicate<V> delegate;

    @Override
    public boolean test(Attempt<V> attempt) {
        if (!attempt.hasResult()) {
            return false;
        } else {
            V result = attempt.getResult();
            return this.delegate.test(result);
        }
    }
}
