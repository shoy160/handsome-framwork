package cn.handsome.rocketmq.annotation;

import cn.handsome.rocketmq.config.HandsomeRocketMqConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author shoy
 * @date 2021/10/28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(value = {HandsomeRocketMqConfig.class})
public @interface EnableRocket {
}
