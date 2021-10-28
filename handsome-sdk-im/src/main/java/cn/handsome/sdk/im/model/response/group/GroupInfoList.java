package cn.handsome.sdk.im.model.response.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/8/31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoList {
    private int total;
    private long next;
    private List<GroupInfoResp.GroupInfo> groups;
}
