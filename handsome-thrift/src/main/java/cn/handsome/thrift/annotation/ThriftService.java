package cn.handsome.thrift.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * todo
 *
 * @author shay
 * @date 2021/5/28
 **/
@Component
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ThriftService {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
