package cn.handsome.jdbc.manage;

import cn.handsome.jdbc.model.JdbcConfig;
import cn.handsome.jdbc.model.JdbcQueryCmd;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author luoyong
 * @date 2025/8/20
 */
@Slf4j
public final class JdbcBuilder {
    static {
        // 加载驱动
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            Class.forName("io.transwarp.jdbc.InceptorDriver");
        } catch (ClassNotFoundException ignored) {
        }
        DriverManager.setLogWriter(new PrintWriter(System.out));
    }

    public static void execute(JdbcConfig config, Consumer<JdbcTemplate> consumer) {
        JdbcTemplate template;
        template = build(config);
        consumer.accept(template);
    }

    public static <T> T query(JdbcConfig config, Function<JdbcTemplate, T> consumer) {
        JdbcTemplate template;
        template = build(config);
        return consumer.apply(template);
    }

    @SneakyThrows
    public static JdbcTemplate build(JdbcConfig config) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if (StrUtil.isNotBlank(config.getDriverClass())) {
            dataSource.setDriverClassName(config.getDriverClass());
        }
        dataSource.setUrl(config.getUrl());
        if (StrUtil.isNotBlank(config.getUsername()) || StrUtil.isNotBlank(config.getPassword())) {
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());
        }
        return new JdbcTemplate(dataSource);
    }

    public static Map<String, Object> queryData(JdbcQueryCmd config) {
        JdbcTemplate jdbcTemplate = build(config);
        String sql = config.getSql();
        Map<String, Object> params = BeanUtil.beanToMap(config.getParams());
        List<Map<String, Object>> list;
        if (MapUtil.isEmpty(params)) {
            list = jdbcTemplate.queryForList(sql);
        } else {
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            list = template.queryForList(sql, params);
        }
        log.info("query from db data size={}", list.size());
        HashMap<String, Object> result = new HashMap<>(2);
        result.put("size", list.size());
        result.put("data", list);
        return result;
    }

    public static int execute(JdbcQueryCmd config) {
        JdbcTemplate jdbcTemplate = build(config);
        String sql = config.getSql();
        Object paramValue = config.getParams();
        boolean isSingle = !(paramValue instanceof Iterable);
        List<Map<String, Object>> params = convertToMapList(paramValue);
        int result = 0;
        if (CollUtil.isEmpty(params)) {
            result = jdbcTemplate.update(sql);
        } else {
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
            for (Map<String, Object> param : params) {
                try {
                    result += template.update(sql, param);
                } catch (Exception e) {
                    log.info("执行 SQL 语句异常", e);
                    if (isSingle) {
                        throw new RuntimeException(String.format("执行 SQL 语句异常: %s", ExceptionUtil.getRootCauseMessage(e)));
                    }
                }
            }
        }
        return result;
    }

    private static List<Map<String, Object>> convertToMapList(Object paramValue) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (paramValue instanceof Iterable) {
            ((Iterable<?>) paramValue).forEach(t -> result.add(BeanUtil.beanToMap(t)));
        } else {
            result.add(BeanUtil.beanToMap(paramValue));
        }
        return result;
    }
}
