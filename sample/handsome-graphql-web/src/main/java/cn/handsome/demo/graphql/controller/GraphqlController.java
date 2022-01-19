package cn.handsome.demo.graphql.controller;

import cn.handsome.demo.graphql.model.GraphqlRequest;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shoy
 * @date 2022/1/11
 */
@RestController
@RequestMapping("/graphql")
@RequiredArgsConstructor
public class GraphqlController {
    private final GraphQL graphql;

    @PostMapping
    public Object execute(@RequestBody GraphqlRequest request) {
        ExecutionInput input = ExecutionInput.newExecutionInput()
                .query(request.getQuery())
                .variables(request.getVariables())
                .build();

        ExecutionResult executionResult = graphql.execute(input);
        List<GraphQLError> errors = executionResult.getErrors();

        if (errors != null && !errors.isEmpty()) {
            Map<String, Object> result = new HashMap<>(1);
            result.put("errors", errors);
            return result;
        }
        return executionResult.getData();
    }
}
