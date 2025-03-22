package cn.handsome.demo.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * todo
 *
 * @author shay
 * @date 2022/8/6
 **/
@SpringBootApplication
@ComponentScan("cn.handsome")
@MapperScan(value = "cn.handsome.demo.sharding.dao")
public class ShardingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }
}
