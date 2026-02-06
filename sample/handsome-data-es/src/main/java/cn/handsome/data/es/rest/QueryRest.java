package cn.handsome.data.es.rest;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/12/10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/es/query")
public class QueryRest {

    private final ElasticsearchOperations operations;

    @GetMapping
    public Object query() {
        BoolQuery.Builder builder = QueryBuilders.bool();
        NativeQuery query = new NativeQueryBuilder()
                .withQuery(builder.build()._toQuery())
                .withAggregation("group", Aggregation.of(
                        a -> a.terms(t -> t.field("workflowType.keyword"))
                ))

                .build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of("conductor03_workflow-*");

        SearchHits<Object> searchHits =
                operations.search(query, Object.class, indexCoordinates);
        Map<String, Long> result = new HashMap<>();
        if (searchHits.hasAggregations()) {
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
            Map<String, ElasticsearchAggregation> aggregationMap = aggregations.aggregationsAsMap();
            ElasticsearchAggregation group = aggregationMap.get("group");
            Aggregate aggregate = group.aggregation().getAggregate();
            List<StringTermsBucket> buckets = aggregate.sterms().buckets().array();
            for (StringTermsBucket bucket : buckets) {
                String key = bucket.key().stringValue();
                long count = bucket.docCount();
                result.put(key, count);
            }
        }

        return result;
    }
}
