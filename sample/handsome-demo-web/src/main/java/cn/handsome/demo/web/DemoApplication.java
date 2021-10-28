package cn.handsome.demo.web;

import cn.handsome.core.Constants;
import cn.handsome.demo.DemoConstants;
import cn.handsome.thrift.annotation.EnableThriftServer;
import cn.handsome.web.HandsomeApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shoy
 * @date 2021/05/28
 */
@EnableThriftServer
@SpringBootApplication
@ComponentScan(Constants.BASE_PACKAGES)
@MapperScan(value = DemoConstants.MAPPER_PACKAGE)
public class DemoApplication {
    public static void main(String[] args) {
        HandsomeApplication.run(DemoConstants.SERVICE_NAME, DemoApplication.class, args);
    }
}
