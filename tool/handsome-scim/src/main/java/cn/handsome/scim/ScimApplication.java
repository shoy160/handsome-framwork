package cn.handsome.scim;

import cn.handsome.web.HandsomeApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@SpringBootApplication(scanBasePackages = "cn.handsome")
public class ScimApplication {
    public static void main(String[] args) {
        HandsomeApplication.run("SCIM", ScimApplication.class, args);
    }
}
