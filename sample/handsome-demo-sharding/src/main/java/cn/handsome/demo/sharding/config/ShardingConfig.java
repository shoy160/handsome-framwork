package cn.handsome.demo.sharding.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * todo
 *
 * @author shay
 * @date 2022/8/6
 **/
public class ShardingConfig {

    public void config() throws SQLException {
        Map<String, DataSource> dataSources = createDataSources();
        Collection<RuleConfiguration> ruleConfigs = createRules();
        Properties properties = new Properties();
        DataSource dataSource = ShardingSphereDataSourceFactory
                .createDataSource(dataSources, ruleConfigs, properties);
    }

    private Collection<RuleConfiguration> createRules() {
        ShardingRuleConfiguration config = new ShardingRuleConfiguration();
        return new ArrayList<>();
    }

    private Map<String, DataSource> createDataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("ds1", createDataSource("db_sharding01"));
        dataSourceMap.put("ds2", createDataSource("db_sharding02"));
        return dataSourceMap;
    }

    private DataSource createDataSource(String dbName) {
        HikariDataSource source = new HikariDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setJdbcUrl(String.format("jdbc:mysql://120.53.243.146:13306/%s", dbName));
        source.setUsername("root");
        source.setPassword("hs@123456");
        return source;
    }
}
