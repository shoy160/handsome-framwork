package cn.handsome.data.es.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;


/**
 *
 * @author luoyong
 * @date 2025/12/10
 */
@Configuration
@RequiredArgsConstructor
public class EsConfiguration {
    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        List<String> uris = elasticsearchProperties.getUris();
        HttpHost httpHost = new HttpHost("localhost", 19200, "http");
        RestClient restClient = RestClient.builder(httpHost)
                .setHttpClientConfigCallback(b -> {
                    var credentials = new UsernamePasswordCredentials("elastic", "526Y80seHtWMaOfN04n776sK");
                    var provider = new BasicCredentialsProvider();
                    provider.setCredentials(AuthScope.ANY, credentials);
                    b.setDefaultCredentialsProvider(provider);
                    b.addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                        request.setHeader("Content-Type", "application/json");
                        request.setHeader("Accept", "application/json");
                    });
                    b.addInterceptorFirst((HttpResponseInterceptor) (response, context) -> {
                        response.addHeader("X-Elastic-Product", "Elasticsearch");
                    });
                    return b;
                })
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return new ElasticsearchTemplate(client);
    }
}
