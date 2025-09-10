package cn.handsome.scim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Getter
@Setter
public class ScimPatch {
    @JsonProperty("schemas")
    private List<String> schemas;

    @JsonProperty("Operations")
    private List<Operation> operations = new ArrayList<>();

    public ScimPatch() {
        this.schemas = Collections.singletonList(BaseResource.SCHEMAS_PATCH_OP);
    }

    @Getter
    @Setter
    public static class Operation {
        @JsonProperty("op")
        private String op; // add, remove, replace

        @JsonProperty("path")
        private String path;

        @JsonProperty("value")
        private Object value;
    }
}
