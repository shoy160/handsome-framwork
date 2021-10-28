package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class GroupCreateResp extends RestResp {
    /**
     * 创建成功之后的群 ID
     */
    @JsonProperty("GroupId")
    private String groupId;
}
