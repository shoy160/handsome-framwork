package cn.handsome.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * todo
 *
 * @author shay
 * @date 2023/10/18
 **/
@SpringBootApplication
@ComponentScan(basePackages = "cn.handsome")
public class ConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
