package cn.handsome.jdbc;

import cn.handsome.web.HandsomeApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 *
 * @author luoyong
 * @date 2025/8/20
 */
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class}
)
public class JdbcApplication {
    public static void main(String[] args) {
        HandsomeApplication.run("handsome-jdbc", JdbcApplication.class, args);
    }
}
