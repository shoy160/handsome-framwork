package cn.handsome.scim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@EqualsAndHashCode(callSuper = true)
public class ServiceProviderConfig extends BaseResource {
    @JsonProperty("documentationUri")
    private String documentationUri;

    @JsonProperty("patch")
    private OperationSupport patch = new OperationSupport(true);

    @JsonProperty("bulk")
    private BulkSupport bulk = new BulkSupport(false, 1000);

    @JsonProperty("filter")
    private FilterSupport filter = new FilterSupport(true, 100);

    @JsonProperty("changePassword")
    private OperationSupport changePassword = new OperationSupport(true);

    @JsonProperty("sort")
    private OperationSupport sort = new OperationSupport(true);

    @JsonProperty("etag")
    private OperationSupport etag = new OperationSupport(false);

    @JsonProperty("authenticationSchemes")
    private List<AuthenticationScheme> authenticationSchemes = new ArrayList<>();

    public ServiceProviderConfig() {
        getSchemas().add(SCHEMAS_PROVIDER_CONFIG);

        // 添加基本认证方案
        AuthenticationScheme scheme = new AuthenticationScheme();
        scheme.setType("oauth2");
        scheme.setName("OAuth 2.0");
        scheme.setDescription("OAuth 2.0 authentication");
        scheme.setSpecUri("https://tools.ietf.org/html/rfc6749");
        scheme.setDocumentationUri("https://example.com/docs/auth");
        authenticationSchemes.add(scheme);
    }

    @Data
    public static class OperationSupport {
        @JsonProperty("supported")
        private boolean supported;

        public OperationSupport(boolean supported) {
            this.supported = supported;
        }
    }

    @Data
    public static class BulkSupport {
        @JsonProperty("supported")
        private boolean supported;

        @JsonProperty("maxOperations")
        private int maxOperations;

        public BulkSupport(boolean supported, int maxOperations) {
            this.supported = supported;
            this.maxOperations = maxOperations;
        }
    }

    @Data
    public static class FilterSupport {
        @JsonProperty("supported")
        private boolean supported;

        @JsonProperty("maxResults")
        private int maxResults;

        public FilterSupport(boolean supported, int maxResults) {
            this.supported = supported;
            this.maxResults = maxResults;
        }
    }

    @Data
    @NoArgsConstructor
    public static class AuthenticationScheme {

        @JsonProperty("type")
        private String type;

        @JsonProperty("schema")
        private String schema;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("specUri")
        private String specUri;

        @JsonProperty("documentationUri")
        private String documentationUri;

        public AuthenticationScheme(String schema, String name, String description, String documentationUri, String type) {
            this.schema = schema;
            this.name = name;
            this.description = description;
            this.documentationUri = documentationUri;
            this.type = type;
        }
    }
}


