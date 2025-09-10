package cn.handsome.scim.model;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResource {
    public static final String SCHEMAS_PROVIDER_CONFIG = "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig";

    public static final String SCHEMAS_GROUP = "urn:ietf:params:scim:schemas:core:2.0:Group";
    public static final String SCHEMAS_USER = "urn:ietf:params:scim:schemas:core:2.0:User";
    public static final String SCHEMAS_PATCH_OP = "urn:ietf:params:scim:api:messages:2.0:PatchOp";

    public static final String SCHEMAS_LIST_RESP = "urn:ietf:params:scim:api:messages:2.0:ListResponse";

    private String id;

    @JsonProperty("externalId")
    private String externalId;

    @JsonProperty("schemas")
    private List<String> schemas = new ArrayList<>();

    public BaseResource() {
        this.id = IdUtil.fastUUID();
    }
}
