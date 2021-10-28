package cn.handsome.thrift.annotation;

import cn.handsome.thrift.server.ThriftServerRunner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启Thrift服务
 *
 * @author shay
 * @date 2021/5/31
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(value = {ThriftServerRunner.class})
public @interface EnableThriftServer {
}
