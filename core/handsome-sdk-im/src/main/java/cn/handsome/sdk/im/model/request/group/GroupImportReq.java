package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupImportReq {
    /**
     * 用户自定义群组外显 ID（选填）
     */
    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 群主 ID
     */
    @JsonProperty("Owner_Account")
    private String ownerAccount;
    /**
     * 群组类型 (必填)
     */
    @JsonProperty("Type")
    private GroupTypeEnum type;
    /**
     * 群名称，最长30字节 (必填)
     */
    @JsonProperty("Name")
    private String name;
    /**
     * 群组的创建时间(时间戳秒) (选填)
     */
    @JsonProperty("CreateTime")
    private Integer createTime;

    @JsonProperty("Introduction")
    private String introduction;
    @JsonProperty("Notification")
    private String notification;
    @JsonProperty("FaceUrl")
    private String faceUrl;
    @JsonProperty("MaxMemberCount")
    private Integer maxMemberCount;
    @JsonProperty("ApplyJoinOption")
    private String applyJoinOption;
    @JsonProperty("AppDefinedData")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AppDefinedDataDTO> appDefinedData;

    @NoArgsConstructor
    @Data
    public static class AppDefinedDataDTO {
        @JsonProperty("Key")
        private String key;
        @JsonProperty("Value")
        private String value;
    }
}
