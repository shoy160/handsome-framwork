package cn.handsome.demo.web.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shoy
 * @date 2021/9/14
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
    private String name;
    private List<String> list;
}
