package cn.handsome.tool.config;

import cn.handsome.tool.ipregion.Searcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shoy
 * @date 2021/12/7
 */
@Configuration
public class ToolConfig {

    @Bean
    public Searcher searcher(IpRegionProperties config) {
        return new Searcher(config);
    }
}
