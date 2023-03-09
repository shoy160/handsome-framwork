package cn.handsome.demo.domain.po;

import cn.handsome.data.handler.EnumTypeHandler;
import cn.handsome.data.handler.JsonTypeHandler;
import cn.handsome.demo.domain.enums.GenderEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.handsome.data.domain.po.BaseAuditPO;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Getter
@Setter
@TableName(value = "user", autoResultMap = true)
public class UserPO extends BaseAuditPO {
    @TableId(value = "id")
    private long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "gender", typeHandler = EnumTypeHandler.class)
    private GenderEnum gender;

    @TableField(value = "tags", typeHandler = JacksonTypeHandler.class)
    private List<TagDTO> tags;

    @Getter
    @Setter
    @ToString

    public static class TagDTO {
        private Integer id;
        private String name;
    }
}
