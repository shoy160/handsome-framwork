package cn.handsome.sdk.im.model.request.sns;

import cn.handsome.sdk.im.model.enums.FriendAddTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class FriendAddReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("AddFriendItem")
    private List<FriendItem> items;
    @JsonProperty("AddType")
    private FriendAddTypeEnum type = FriendAddTypeEnum.Add_Type_Both;
    @JsonProperty("ForceAddFlags")
    private int forceAdd = 0;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FriendItem {
        /**
         * 好友的 UserID
         */
        @JsonProperty("To_Account")
        private String toAccount;
        /**
         * From_Account 对 To_Account 的好友备注
         */
        @JsonProperty("Remark")
        private String remark;
        /**
         * From_Account 对 To_Account 的分组信息
         */
        @JsonProperty("GroupName")
        private String group;
        /**
         * 加好友来源字段
         */
        @JsonProperty("AddSource")
        private String source;
        /**
         * From_Account 和 To_Account 形成好友关系时的附言信息
         */
        @JsonProperty("AddWording")
        private String wording;

        public String getSource() {
            return "AddSource_Type_".concat(this.source);
        }
    }
}
