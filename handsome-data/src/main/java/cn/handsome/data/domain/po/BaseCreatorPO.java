package cn.handsome.data.domain.po;

import cn.handsome.core.domain.HaveCreator;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/12/21
 */
@Getter
@Setter
public class BaseCreatorPO<T> extends BaseAuditPO implements HaveCreator<T> {
    /**
     * 创建者ID
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private T creatorId;
}
