package cn.handsome.core.http.annotation;

import java.lang.annotation.*;

/**
 * Option
 *
 * @author shay
 * @date 2021/3/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Option {
    String value();
}
