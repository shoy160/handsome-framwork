package cn.handsome.core.policy.predicate;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public class Predicates {
    public static <T> Predicate<T> alwaysFalse() {
        return t -> false;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return Predicate.isEqual(true);
    }

    public static <T> Predicate<T> or(Predicate<T> prev, Predicate<? super T> next) {
        if (Objects.isNull(prev)) {
            prev = alwaysFalse();
        }
        if (Objects.isNull(next)) {
            return prev;
        }
        return prev.or(next);
    }

    public static <T> Predicate<T> and(Predicate<T> prev, Predicate<? super T> next) {
        if (Objects.isNull(prev)) {
            prev = alwaysTrue();
        }
        if (Objects.isNull(next)) {
            return prev;
        }
        return prev.and(next);
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}
