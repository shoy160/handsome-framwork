package cn.handsome.web;

import cn.handsome.core.AppContext;
import cn.handsome.core.Constants;
import cn.handsome.core.launcher.LauncherManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

import java.net.URL;
import java.util.Objects;
import java.util.Properties;

/**
 * 应用启动类
 *
 * @author shay
 * @date 2020/7/15
 */
@Slf4j
public class HandsomeApplication extends SpringApplication {
    private final String appName;

    public HandsomeApplication(String appName, Class<?>... primarySources) {
        super(primarySources);
        this.appName = appName;
    }

    @Override
    public ConfigurableApplicationContext run(String... args) {
        Assert.hasText(appName, "[appName]不能为空");
        AppContext.init(appName);
        LauncherManager manager = LauncherManager.getInstance();
        manager.preLoad();
        this.addListeners((ApplicationListener<ApplicationFailedEvent>) applicationEvent -> {
            manager.onDestroy();
        });
        this.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) applicationEvent -> {
            this.setBasicConfig(applicationEvent.getEnvironment());
        });
        ConfigurableApplicationContext context = super.run(args);
        manager.onLoad();
        return context;
    }

    @Override
    protected void configureEnvironment(ConfigurableEnvironment environment, String[] args) {
        environment.setDefaultProfiles(Constants.MODE_DEV, Constants.MODE_SECRET);
        super.configureEnvironment(environment, args);
    }

    public static ConfigurableApplicationContext run(String appName, Class<?> primarySource, String... args) {
        return run(appName, new Class[]{primarySource}, args);
    }

    public static ConfigurableApplicationContext run(String appName, Class<?>[] primarySources, String[] args) {
        return (new HandsomeApplication(appName, primarySources)).run(args);
    }

    private void setBasicConfig(ConfigurableEnvironment environment) {
        if (null == environment) {
            return;
        }
        MutablePropertySources propertySources = environment.getPropertySources();
        final String basicName = "basicProperties";
        PropertySource<?> basic = propertySources.get(basicName);
        if (Objects.nonNull(basic)) {
            propertySources.remove(basicName);
        }
        Properties props = new Properties();
        props.setProperty("spring.application.name", this.appName);
        props.setProperty("spring.messages.encoding", "UTF-8");
        props.setProperty("handsome.version", Constants.APPLICATION_VERSION);
        //优雅停机
        props.setProperty("server.shutdown", "graceful");
        props.setProperty("spring.lifecycle.timeout-per-shutdown-phase", "20s");
        //开启健康检测
        props.setProperty("management.endpoint.health.probes.enabled", "true");
        LauncherManager.getInstance().config(environment, props);
        basic = new PropertiesPropertySource(basicName, props);
        propertySources.addLast(basic);
        AppContext.initAppMode(environment.getActiveProfiles());
        String startJarPath = Constants.STR_EMPTY;
        URL resource = HandsomeApplication.class.getResource("/");
        if (Objects.nonNull(resource)) {
            startJarPath = resource.getPath().split("!")[0];
        }
        log.info("服务正在启动，读取到的环境变量:[{}]，jar地址:[{}]", AppContext.getAppMode(), startJarPath);
    }
}
