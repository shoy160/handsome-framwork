package cn.handsome.demo.web.model.vo;

import cn.handsome.demo.domain.enums.GenderEnum;
import cn.handsome.demo.domain.po.UserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Getter
@Setter
@ToString
@ApiModel(description = "用户信息")
public class UserVO implements Serializable {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("Bool")
    private Boolean bool;
    @ApiModelProperty("Int")
    private Integer integer;
    @ApiModelProperty("Object")
    private Object obj;
    @ApiModelProperty("List")
    private List<String> list;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    private GenderEnum gender;

    private List<UserPO.TagDTO> tags;

    public UserVO() {
        this.updateTime = new Date();
    }
}
