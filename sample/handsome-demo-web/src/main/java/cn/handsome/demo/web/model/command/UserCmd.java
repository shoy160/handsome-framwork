package cn.handsome.demo.web.model.command;

import cn.handsome.demo.domain.enums.GenderEnum;
import cn.handsome.demo.domain.po.UserPO;
import cn.handsome.web.model.command.MobileCmd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Getter
@Setter
public class UserCmd extends MobileCmd {
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty("姓名")
    private String name;

    private Date time;

    @NotNull(message = "性别异常")
    private GenderEnum gender;

    private UserPO.TagDTO[] tags;
}
