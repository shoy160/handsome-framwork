package cn.handsome.swagger;

import cn.handsome.core.Constants;
import cn.handsome.web.HandsomeApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shay
 * @date 2020/8/22
 */

@SpringBootApplication
@ComponentScan(value = Constants.BASE_PACKAGES)
public class SwaggerApplication {
    public static void main(String[] args) {
        HandsomeApplication.run("handsome-swagger", SwaggerApplication.class, args);
    }
}
