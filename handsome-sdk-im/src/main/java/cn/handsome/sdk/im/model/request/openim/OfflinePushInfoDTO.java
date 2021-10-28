package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shoy
 * @date 2021/7/8
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfflinePushInfoDTO {
    @JsonProperty("PushFlag")
    private Integer pushFlag;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("Ext")
    private String ext;
    @JsonProperty("AndroidInfo")
    private AndroidInfoDTO androidInfo;
    @JsonProperty("ApnsInfo")
    private ApnsInfoDTO apnsInfo;

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AndroidInfoDTO {
        @JsonProperty("Sound")
        private String sound;
        @JsonProperty("HuaWeiChannelID")
        private String huaWeiChannelID;
        @JsonProperty("XiaoMiChannelID")
        private String xiaoMiChannelID;
        @JsonProperty("OPPOChannelID")
        private String oppoChannelID;
        @JsonProperty("GoogleChannelID")
        private String googleChannelID;
        @JsonProperty("VIVOClassification")
        private Integer vivoClassification;
    }

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApnsInfoDTO {
        @JsonProperty("Sound")
        private String sound;
        @JsonProperty("BadgeMode")
        private Integer badgeMode;
        @JsonProperty("Title")
        private String title;
        @JsonProperty("SubTitle")
        private String subTitle;
        @JsonProperty("Image")
        private String image;
        @JsonProperty("MutableContent")
        private Integer mutableContent;
    }
}
