package cn.handsome.core.compare;

import cn.handsome.core.compare.enums.ConditionOp;
import cn.handsome.core.utils.TypeUtils;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@RequiredArgsConstructor
public abstract class BaseCompare implements ICompare {
    private final ConditionOp operation;

    @Override
    public ConditionOp getOperation() {
        return this.operation;
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
}
