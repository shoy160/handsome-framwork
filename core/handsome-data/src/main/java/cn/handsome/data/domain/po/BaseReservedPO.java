package cn.handsome.data.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import cn.handsome.core.domain.HaveReserved;
import lombok.Getter;
import lombok.Setter;

/**
 * 扩展字段 & 审计 基础持久化对象
 *
 * @author shay
 * @date 2020/10/13
 */
@Getter
@Setter
public abstract class BaseReservedPO extends BaseAuditPO implements HaveReserved {

    private static final long serialVersionUID = -1785863488488971606L;

    /**
     * 保留字段1
     */
    @TableField("reserved1")
    private String reserved1;

    /**
     * 保留字段2
     */
    @TableField("reserved2")
    private String reserved2;
}
