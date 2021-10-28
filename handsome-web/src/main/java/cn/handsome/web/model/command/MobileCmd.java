package cn.handsome.web.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Getter
@Setter
public class MobileCmd extends BaseCmd {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[1-9][0-9]{9}$", message = "手机号格式不匹配")
    @ApiModelProperty("手机号")
    private String mobile;
}
