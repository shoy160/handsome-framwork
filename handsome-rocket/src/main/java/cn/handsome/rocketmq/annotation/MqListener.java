package cn.handsome.rocketmq.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author shoy
 * @date 2021/6/10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MqListener {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";

    /**
     * topic
     *
     * @return topic
     */
    String topic();

    /**
     * tag
     *
     * @return tag
     */
    String tag() default "*";
}
