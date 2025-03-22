package cn.handsome.demo.sharding.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * todo
 *
 * @author shay
 * @date 2022/8/6
 **/
@Getter
@Setter
@TableName("record")
public class RecordPO {
    @TableId
    private String id;

    @TableField("app_id")
    private String appId;
    @TableField("scene_id")
    private Long sceneId;
    @TableField("event_id")
    private Long eventId;
    @TableField("user_id")
    private String userId;
    private String account;
    @TableField("client_ip")
    private String clientIp;
    @TableField("user_agent")
    private String userAgent;
}
