package cn.handsome.data.domain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cn.handsome.core.domain.HaveDate;
import cn.handsome.core.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shay
 * @date 2021/3/5
 */
@Getter
@Setter
@ApiModel
@JsonPropertyOrder()
public abstract class BaseDateDTO extends BaseDTO implements HaveDate {

    private static final long serialVersionUID = 4510795506100500532L;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
