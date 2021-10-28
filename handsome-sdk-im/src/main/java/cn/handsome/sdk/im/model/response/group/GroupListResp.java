package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class GroupListResp extends RestResp {

    /**
     * 当前的群组总数
     */
    @JsonProperty("TotalCount")
    private Integer totalCount;

    @JsonProperty("Next")
    private Long next;

    @JsonProperty("GroupIdList")
    private List<GroupId> groupIdList;

    @Getter
    @Setter
    public static class GroupId {
        @JsonProperty("GroupId")
        private String groupId;
    }
}
