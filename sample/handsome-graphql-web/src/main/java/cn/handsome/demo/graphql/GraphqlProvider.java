package cn.handsome.demo.graphql;

import cn.handsome.demo.graphql.resolver.ItemResolver;
import cn.handsome.demo.graphql.resolver.Mutation;
import cn.handsome.demo.graphql.resolver.Query;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author shoy
 * @date 2022/1/11
 */
@Component
public class GraphqlProvider {

    private GraphQL graphql;

    @Bean
    public GraphQL graphql() {
        return this.graphql;
    }

    @PostConstruct
    public void init() {
        GraphQLSchema schema = SchemaParser.newParser()
                .file("graphql/base.graphql")
                .resolvers(new Query(), new Mutation())
                .file("graphql/item.graphql")
                .resolvers(new ItemResolver())
                .build()
                .makeExecutableSchema();
        this.graphql = GraphQL.newGraphQL(schema).build();
    }
}
