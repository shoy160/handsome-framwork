package cn.handsome.core.http.annotation;

import java.lang.annotation.*;

/**
 * @author shay
 * @date 2021/3/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface Xml {
}
