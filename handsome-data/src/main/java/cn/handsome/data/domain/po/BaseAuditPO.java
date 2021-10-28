package cn.handsome.data.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import cn.handsome.core.domain.HaveDate;
import cn.handsome.core.domain.SoftDelete;
import cn.handsome.core.domain.entity.BasePO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 审计 基础持久化对象
 *
 * @author shay
 * @date 2020/10/13
 */
@Getter
@Setter
public abstract class BaseAuditPO extends BasePO implements HaveDate, SoftDelete {

    private static final long serialVersionUID = -1785863488488971606L;

    /**
     * 是否删除 0否 1是
     */
    @TableLogic
    @TableField("is_del")
    private boolean isDel;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @Version
    @TableField(value = "update_time")
    private Date updateTime;
}
