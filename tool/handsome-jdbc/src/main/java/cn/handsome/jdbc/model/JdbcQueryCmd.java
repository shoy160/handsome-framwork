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
public class JdbcQueryCmd extends JdbcConfig {
    private String sql;
    private Object params;
}
