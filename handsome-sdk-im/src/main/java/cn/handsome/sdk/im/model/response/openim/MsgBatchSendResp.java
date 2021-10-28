package cn.handsome.sdk.im.model.response.openim;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/21
 */
@Getter
@Setter
public class MsgBatchSendResp extends RestResp {

    @JsonProperty("MsgKey")
    private String msgKey;

    @JsonProperty("ErrorList")
    private List<ErrorListDTO> errorList;

    @NoArgsConstructor
    @Data
    public static class ErrorListDTO {
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("ErrorCode")
        private Integer errorCode;
    }
}
