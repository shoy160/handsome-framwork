package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 撤回群消息
 *
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
public class GroupMsgRecallReq {
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("MsgSeqList")
    private List<MsgSeqListDTO> msgSeqList;

    @NoArgsConstructor
    @Data
    public static class MsgSeqListDTO {
        @JsonProperty("MsgSeq")
        private Integer msgSeq;
    }
}
