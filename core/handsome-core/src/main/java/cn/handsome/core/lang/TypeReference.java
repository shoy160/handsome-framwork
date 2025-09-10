package cn.handsome.core.lang;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Type 参考
 *
 * @author shay
 * @date 2021/4/6
 */
@Slf4j
public abstract class TypeReference<T> {
    private final Type gType;

    protected TypeReference() {
        Type superType = this.getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) superType).getActualTypeArguments();
            this.gType = arguments[0];
        } else {
            log.warn(String.format("Warn: %s's superclass not ParameterizedType", this.getClass().getSimpleName()));
            this.gType = Object.class;
        }
    }

    public Type getType() {
        return this.gType;
    }

    public Class<T> getClazz() {
        return (Class<T>) this.gType;
    }
}
