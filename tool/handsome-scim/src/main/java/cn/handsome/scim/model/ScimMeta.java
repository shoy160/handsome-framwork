package cn.handsome.scim.model;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Getter
@Setter
public class ScimMeta {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("created")
    private String created;

    @JsonProperty("lastModified")
    private String lastModified;

    @JsonProperty("location")
    private String location;

    @JsonProperty("version")
    private String version;

    public ScimMeta() {
        this.created = LocalDateTime.now().toString();
        this.changed();
    }

    public ScimMeta changed() {
        this.lastModified = LocalDateTime.now().toString();
        this.version = IdUtil.fastSimpleUUID();
        return this;
    }

    public ScimMeta(String resourceType, String location) {
        this();
        this.resourceType = resourceType;
        this.location = location;
    }
}
