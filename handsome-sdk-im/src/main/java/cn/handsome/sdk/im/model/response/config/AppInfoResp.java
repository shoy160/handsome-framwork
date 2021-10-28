package cn.handsome.sdk.im.model.response.config;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppInfoResp extends RestResp {

    @JsonProperty("Result")
    private List<ResultDTO> result;

    @NoArgsConstructor
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResultDTO {
        @JsonProperty("APNSMsgNum")
        private String apnsMsgNum;
        @JsonProperty("ActiveUserNum")
        private String activeUserNum;
        @JsonProperty("AppId")
        private String appId;
        @JsonProperty("AppName")
        private String appName;
        @JsonProperty("C2CAPNSMsgNum")
        private String c2cApnsMsgNum;
        @JsonProperty("C2CDownMsgNum")
        private String c2cDownMsgNum;
        @JsonProperty("C2CSendMsgUserNum")
        private String c2cSendMsgUserNum;
        @JsonProperty("C2CUpMsgNum")
        private String c2cUpMsgNum;
        @JsonProperty("CallBackReq")
        private String callBackReq;
        @JsonProperty("CallBackRsp")
        private String callBackRsp;
        @JsonProperty("ChainDecrease")
        private String chainDecrease;
        @JsonProperty("ChainIncrease")
        private String chainIncrease;
        @JsonProperty("Company")
        private String company;
        @JsonProperty("Date")
        private String date;
        @JsonProperty("DownMsgNum")
        private String downMsgNum;
        @JsonProperty("GroupAPNSMsgNum")
        private String groupApnsMsgNum;
        @JsonProperty("GroupAllGroupNum")
        private String groupAllGroupNum;
        @JsonProperty("GroupDestroyGroupNum")
        private String groupDestroyGroupNum;
        @JsonProperty("GroupDownMsgNum")
        private String groupDownMsgNum;
        @JsonProperty("GroupJoinGroupTimes")
        private String groupJoinGroupTimes;
        @JsonProperty("GroupNewGroupNum")
        private String groupNewGroupNum;
        @JsonProperty("GroupQuitGroupTimes")
        private String groupQuitGroupTimes;
        @JsonProperty("GroupSendMsgGroupNum")
        private String groupSendMsgGroupNum;
        @JsonProperty("GroupSendMsgUserNum")
        private String groupSendMsgUserNum;
        @JsonProperty("GroupUpMsgNum")
        private String groupUpMsgNum;
        @JsonProperty("LoginTimes")
        private String loginTimes;
        @JsonProperty("LoginUserNum")
        private String loginUserNum;
        @JsonProperty("MaxOnlineNum")
        private String maxOnlineNum;
        @JsonProperty("RegistUserNumOneDay")
        private String registUserNumOneDay;
        @JsonProperty("RegistUserNumTotal")
        private String registUserNumTotal;
        @JsonProperty("SendMsgUserNum")
        private String sendMsgUserNum;
        @JsonProperty("UpMsgNum")
        private String upMsgNum;
    }
}
