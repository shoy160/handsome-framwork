package cn.handsome.demo.web.model.manage;

import cn.handsome.core.domain.dto.BaseDTO;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
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
public class ImGroupMemberVO extends BaseDTO {
    private String account;
    private GroupRoleEnum role;
    private Date joinTime;
    private String nameCard;
    private String nick;
    private String faceUrl;
}
