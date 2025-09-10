package cn.handsome.scim.model;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class ScimGroup extends BaseResource {

    @JsonProperty("name")
    private String name;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("members")
    private List<ScimMember> members = new ArrayList<>();

    @JsonProperty("meta")
    private ScimMeta meta;

    public String getDisplayName() {
        return StrUtil.isBlank(displayName) ? this.name : displayName;
    }

    public ScimGroup() {
        this.setSchemas(Collections.singletonList(SCHEMAS_GROUP));
        this.meta = new ScimMeta("Group", "/scim/v2/Groups/" + this.getId());
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.meta.setLocation("/scim/v2/Groups/" + id);
    }
}