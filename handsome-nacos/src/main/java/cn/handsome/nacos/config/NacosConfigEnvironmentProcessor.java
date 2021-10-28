package cn.handsome.nacos.config;

import cn.handsome.core.Constants;
import cn.handsome.core.Context;
import cn.handsome.nacos.utils.NacosHelper;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.yaml.snakeyaml.Yaml;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author shoy
 * @date 2021/6/25
 */
public class NacosConfigEnvironmentProcessor implements EnvironmentPostProcessor, Ordered {

    private static void parseMap(Map<String, Object> map, Properties properties, String prefix) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            key = StrUtil.isEmpty(prefix) ? key : String.format("%s.%s", prefix, key);
            if (value instanceof Map) {
                parseMap((Map<String, Object>) value, properties, key);
            } else {
                if (value instanceof Date) {
                    value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
                }
                properties.put(key, value);
            }
        }
    }

    private static void loadConfig(ConfigurableEnvironment environment, NacosHelper helper, String dataId) {
        String config = helper.getConfig(dataId);
        System.out.printf("[nacos %s]%s%n", dataId, config);
        if (StrUtil.isEmpty(config)) {
            return;
        }
        Map<String, Object> configMap = new Yaml().load(config);
        Properties properties = new Properties();
        parseMap(configMap, properties, Constants.EMPTY_STR);
        PropertySource<?> source = new PropertiesPropertySource(String.format("nacos-%s", dataId), properties);
        environment.getPropertySources().addFirst(source);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String appName = Context.getAppName();
        String mode = Context.getAppMode(environment.getActiveProfiles());
        Binder binder = Binder.get(environment);
        BindResult<NacosProperties> configResult = binder.bind("handsome.nacos", NacosProperties.class);
        NacosProperties nacosConfig = configResult.orElse(new NacosProperties());
        if (null != nacosConfig && StrUtil.isNotEmpty(nacosConfig.getServerAddr())) {
            Properties props = new Properties();
            if (null != nacosConfig.getConfig()) {
                NacosProperties.Config config = nacosConfig.getConfig();
                //Nacos配置中心
                props.setProperty("nacos.config.server-addr", nacosConfig.getServerAddr());
                props.setProperty("nacos.config.namespace", nacosConfig.getNamespace());
                props.setProperty("nacos.config.bootstrap.enable", String.valueOf(nacosConfig.isEnable()));
                props.setProperty("nacos.config.bootstrap.remote-first", String.valueOf(nacosConfig.isRemoteFirst()));
                props.setProperty("nacos.config.auto-refresh", String.valueOf(config.isAutoRefresh()));
                props.setProperty("nacos.config.group", config.getGroup());
                final String applicationConfig = "application";
                List<String> dataIds = new ArrayList<>();
                dataIds.add(appName.concat("_").concat(mode));
                dataIds.add(applicationConfig.concat("_").concat(mode));
                dataIds.add(appName);
                dataIds.add(applicationConfig);
                props.setProperty("nacos.config.data-ids", String.join(",", dataIds));
                props.setProperty("nacos.config.type", config.getType());
            }
            PropertiesPropertySource propertySource = new PropertiesPropertySource("nacosProperties", props);
            environment.getPropertySources().addLast(propertySource);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 6;
    }
}
