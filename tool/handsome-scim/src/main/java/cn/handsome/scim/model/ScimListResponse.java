package cn.handsome.scim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Getter
@Setter
public class ScimListResponse<T> {
    @JsonProperty("schemas")
    private List<String> schemas = Collections.singletonList(BaseResource.SCHEMAS_LIST_RESP);

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("startIndex")
    private int startIndex;

    @JsonProperty("itemsPerPage")
    private int itemsPerPage;

    @JsonProperty("Resources")
    private List<T> resources;
}
