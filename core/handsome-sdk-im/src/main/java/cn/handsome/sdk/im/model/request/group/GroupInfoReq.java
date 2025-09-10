package cn.handsome.sdk.im.model.request.group;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupInfoReq {
    /**
     * 需要拉取的群组列表
     */
    @JsonProperty("GroupIdList")
    private List<String> groupIdList;

    /**
     * 过滤器
     */
    @JsonProperty("ResponseFilter")
    private Filter responseFilter;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Filter {
        /**
         * 基础信息字段过滤器
         */
        @JsonProperty("GroupBaseInfoFilter")
        private List<String> baseInfoFilter;
        @JsonProperty("MemberInfoFilter")
        private List<String> memberInfoFilter;
        @JsonProperty("AppDefinedDataFilter_Group")
        private List<String> groupDefinedFilter;
        @JsonProperty("AppDefinedDataFilter_GroupMember")
        private List<String> memberDefinedFilter;
    }
}
