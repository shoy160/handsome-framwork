package cn.handsome.scim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScimMember {
    @JsonProperty("value")
    private String value;

    @JsonProperty("$ref")
    private String ref;

    @JsonProperty("display")
    private String display;

    public ScimMember(String value, String display) {
        this(value, null, display);
    }

    public static ScimMember user(String value, String display) {
        ScimMember member = new ScimMember(value, display);
        member.userRef();
        return member;
    }

    public static ScimMember group(String value, String display) {
        ScimMember member = new ScimMember(value, display);
        member.groupRef();
        return member;
    }

    public void userRef() {
        ref("Users");
    }

    public void groupRef() {
        ref("Groups");
    }

    public void ref(String type) {
        this.ref = String.format("/scim/v2/%s/%s", type, value);
    }
}
