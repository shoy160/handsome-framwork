package cn.handsome.core.http.annotation;

import cn.handsome.core.http.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * 路由配置
 *
 * @author shay
 * @date 2021/3/18
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Route {
    String value();

    HttpMethod method() default HttpMethod.GET;
}
