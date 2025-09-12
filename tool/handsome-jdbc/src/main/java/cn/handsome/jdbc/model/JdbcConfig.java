package cn.handsome.jdbc.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author luoyong
 * @date 2025/8/20
 */
@Getter
@Setter
public class JdbcConfig {
    private String url;
    private String driverClass;
    private String username;
    private String password;
}
