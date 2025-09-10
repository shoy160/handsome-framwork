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
public class GroupMsgImportResp extends RestResp {

    @JsonProperty("ImportMsgResult")
    private List<ImportMsgResultDTO> importMsgResult;

    @NoArgsConstructor
    @Data
    public static class ImportMsgResultDTO {
        @JsonProperty("MsgSeq")
        private Integer msgSeq;
        @JsonProperty("MsgTime")
        private Integer msgTime;
        @JsonProperty("Result")
        private Integer result;
    }
}
