package cn.handsome.core.compare;

import cn.handsome.core.compare.enums.ConditionOp;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.core.utils.TypeUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseCompare implements ICompare {
    private final ConditionOp operation;

    @Override
    public ConditionOp getOperation() {
        return this.operation;
    }

    protected boolean contains(Object list, Object value, boolean isAny) {
        List<Object> sources = convertToList(list, null);
        List<Object> values = convertToList(value, null);
        if (CollUtil.isEmpty(sources) || CollUtil.isEmpty(values)) {
            return false;
        }
        if (values.size() == 1) {
            return sources.contains(values.get(0));
        }
        if (isAny) {
            // 包含任意
            return values.stream().anyMatch(sources::contains);
        }
        return new HashSet<>(sources).containsAll(values);
    }

    protected boolean objEquals(Object target, Object source) {
        if (target instanceof Iterable) {
            return CollUtil.isEqualList(convertToList(target, null), convertToList(source, null));
        }
        if (target instanceof Map) {
            return CompareFactory.mapEquals(MapUtils.map(target), MapUtils.map(source));
        }
        return ObjUtil.equals(target, source);
    }

    protected List<String> toStringList(Object value) {
        if (Objects.isNull(value)) {
            return new ArrayList<>(0);
        }
        if (TypeUtils.isArray(value)) {
            Collection<?> validItems = (Collection<?>) value;
            return validItems.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(String.valueOf(value));
    }

    protected int compareValue(Object value, Object compareTo) {
        if (value instanceof Number && compareTo instanceof Number) {
            return compareNumber((Number) value, (Number) compareTo);
        } else if (value instanceof Date && compareTo instanceof Date) {
            return ((Date) value).compareTo((Date) compareTo);
        } else if (value instanceof String && compareTo instanceof String) {
            return ((String) value).compareTo((String) compareTo);
        } else {
            throw new RuntimeException(String.format("传入的值 %s 不是数字类型，无法使用比较条件进行判断", value));
        }
    }

    protected static int compareNumber(Number x, Number y) {
        if (isSpecial(x) || isSpecial(y)) {
            return Double.compare(x.doubleValue(), y.doubleValue());
        } else {
            return toBigDecimal(x).compareTo(toBigDecimal(y));
        }
    }

    private static boolean isSpecial(Number x) {
        boolean specialDouble = x instanceof Double
                && (Double.isNaN((Double) x) || Double.isInfinite((Double) x));
        boolean specialFloat = x instanceof Float
                && (Float.isNaN((Float) x) || Float.isInfinite((Float) x));
        return specialDouble || specialFloat;
    }

    private static BigDecimal toBigDecimal(Number number) {
        return NumberUtil.toBigDecimal(number);
    }

    protected static List<Object> convertToList(Object data, String split) {
        List<Object> results = new ArrayList<>();
        if (Objects.isNull(data)) {
            return results;
        }
        try {
            if (data instanceof Iterable) {
                for (Object item : (Iterable<?>) data) {
                    results.add(item);
                }
                return results;
            }
            if (TypeUtils.isString(data)) {
                String strValue = data.toString();
                if (JsonUtils.isArrayJsonStr(strValue)) {
                    return JsonUtils.jsonList(strValue, Object.class);
                }
                if (StrUtil.isNotBlank(split)) {
                    return Arrays.stream(strValue.split(split))
                            .collect(Collectors.toList());
                }
            }
            results.add(data);
            return results;
        } catch (Exception ex) {
            log.warn("列表转换异常", ex);
            return results;
        }
    }
}
