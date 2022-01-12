package cn.handsome.demo.graphql;

import cn.handsome.core.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shoy
 * @date 2022/1/11
 */
@SpringBootApplication
@ComponentScan(Constants.BASE_PACKAGES)
public class GraphqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }
}
