package cn.handsome.scim.model;

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
public class ScimError {
    private List<String> schemas;
    private Integer status;
    private String scimType;
    private String detail;

    public ScimError() {
        this(null, null, null);
    }

    public ScimError(List<String> schemas, Integer status, String scimType, String detail) {
        this.schemas = schemas;
        this.status = status;
        this.scimType = scimType;
        this.detail = detail;
    }

    public ScimError(Integer status, String scimType, String detail) {
        this(Collections.singletonList("urn:ietf:params:scim:api:messages:2.0:Error"), status, scimType, detail);
    }
}
