package cn.handsome.tool;

import cn.handsome.core.Constants;
import cn.handsome.web.HandsomeApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shoy
 * @date 2021/12/7
 */
@SpringBootApplication
@ComponentScan(value = Constants.BASE_PACKAGES)
public class ToolApplication {
    public static void main(String[] args) {
        HandsomeApplication.run("handsome-tool", ToolApplication.class, args);
    }
}
