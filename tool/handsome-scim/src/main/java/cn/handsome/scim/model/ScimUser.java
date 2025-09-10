package cn.handsome.scim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
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
public class ScimUser extends BaseResource {
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("name")
    private Name name;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("emails")
    private List<Email> emails = new ArrayList<>();

    @JsonProperty("groups")
    private List<ScimMember> groups = new ArrayList<>();

    @JsonProperty("meta")
    private ScimMeta meta;

    @JsonProperty("active")
    private Boolean active;

    public ScimUser() {
        setSchemas(Collections.singletonList(SCHEMAS_USER));
        this.meta = new ScimMeta("User", "/scim/v2/Users/" + this.getId());
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.meta.setLocation("/scim/v2/Users/" + id);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Name {
        @JsonProperty("givenName")
        private String givenName;

        @JsonProperty("familyName")
        private String familyName;

        @JsonProperty("formatted")
        private String formatted;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Email {
        @JsonProperty("value")
        private String value;

        @JsonProperty("type")
        private String type;

        @JsonProperty("primary")
        private Boolean primary;

        public Email() {
            this.type = "other";
            this.primary = false;
        }
    }
}
