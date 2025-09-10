package cn.handsome.scim.utils;

import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimMember;
import cn.handsome.scim.model.ScimUser;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public final class ScimUtils {
    public static boolean isEmpty(ScimMember member) {
        return Objects.isNull(member) || StrUtil.isBlank(member.getValue());
    }

    public static <T> List<T> convertList(Object value, Function<Object, T> converter) {
        List<T> list = new ArrayList<>();
        if (value instanceof Iterable) {
            for (Object o : (Iterable<?>) value) {
                list.add(converter.apply(o));
            }
        } else {
            list.add(converter.apply(value));
        }
        return list;
    }

    public static List<Map<String, Object>> convertListMap(Object value) {
        return convertList(value, BeanUtil::beanToMap);
    }

    public static List<ScimMember> convertMembers(Object value) {
        return convertList(value, o -> BeanUtil.toBeanIgnoreError(o, ScimMember.class));
    }
}
