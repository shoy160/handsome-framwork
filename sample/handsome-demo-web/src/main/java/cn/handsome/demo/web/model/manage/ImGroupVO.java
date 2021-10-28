package cn.handsome.demo.web.model.manage;

import cn.handsome.core.domain.dto.BaseDTO;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/8/26
 */
@Getter
@Setter
@ToString
public class ImGroupVO extends BaseDTO {
    private String id;
    private String name;
    private GroupTypeEnum type;
    private String faceUrl;
    private String ownerAccount;
    private Integer memberNum;
    private Integer maxMemberNum;
    private ApplyJoinOptionEnum applyJoinOption;
    private Date createTime;
}
