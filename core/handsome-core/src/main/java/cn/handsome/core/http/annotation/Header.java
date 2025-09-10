package cn.handsome.core.http.annotation;

import java.lang.annotation.*;

/**
 * 请求头配置
 *
 * @author shay
 * @date 2021/3/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Header {
    String key();

    String value();
}
