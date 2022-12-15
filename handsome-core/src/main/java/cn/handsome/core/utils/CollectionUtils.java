package cn.handsome.core.utils;

import cn.handsome.core.lang.Func;
import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author luoyong
 * @date 2022/12/12
 */
public final class CollectionUtils {
    public static <T, V> Collection<V> distinct(Collection<T> sourceList, Func<V, T> valueFunc) {
        if (null == sourceList || null == valueFunc) {
            return new ArrayList<>(0);
        }
        return sourceList
                .stream()
                .map(valueFunc::invoke)
                .filter(ObjectUtil::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());
    }
}
