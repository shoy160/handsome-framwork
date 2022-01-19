package cn.handsome.demo.graphql.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author shoy
 * @date 2022/1/11
 */
@Getter
@Setter
public class GraphqlRequest {
    private String query;
    private Map<String, Object> variables;
}
