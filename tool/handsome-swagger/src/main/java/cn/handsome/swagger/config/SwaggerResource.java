package cn.handsome.swagger.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shay
 * @date 2020/8/22
 */
@Getter
@Setter
@NoArgsConstructor
public class SwaggerResource {
    private String name;
    private String version = "1.0";
    private String location;
    private String url = "";
}
