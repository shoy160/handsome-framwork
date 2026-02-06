package cn.handsome.data.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

/**
 *
 * @author luoyong
 * @date 2025/12/10
 */
@SpringBootApplication(
        exclude = {ElasticsearchDataAutoConfiguration.class}
)
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }
}
