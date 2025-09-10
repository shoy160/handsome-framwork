package cn.handsome.core.http.annotation;

import java.lang.annotation.*;

/**
 * 主机配置
 *
 * @author shay
 * @date 2021/3/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Host {
    String value();
}
