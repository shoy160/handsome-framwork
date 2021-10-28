package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class GroupMsgRecallResp extends RestResp {

    @JsonProperty("RecallRetList")
    private List<RecallRetListDTO> recallRetList;

    @NoArgsConstructor
    @Data
    public static class RecallRetListDTO {
        @JsonProperty("MsgSeq")
        private Integer msgSeq;
        @JsonProperty("RetCode")
        private Integer retCode;
    }
}
