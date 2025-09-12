package cn.handsome.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 认证数据表
 * </p>
 *
 * @author shoy
 * @since 2022-01-13
 */
@Getter
@Setter
@TableName("t_record")
public class TRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 企业
     */
    private String company;

    /**
     * 应用
     */
    private String app;

    /**
     * 场景
     */
    private String scene;

    /**
     * 跟踪ID
     */
    private String traceId;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * Referer
     */
    private String referer;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 账号
     */
    private String account;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * UserAgent
     */
    private String userAgent;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 浏览器
     */
    private String deviceBrowser;

    /**
     * IP 所属国家
     */
    private String ipCountry;

    /**
     * IP 所属省份
     */
    private String ipProvince;

    /**
     * IP 所属城市
     */
    private String ipCity;

    /**
     * IP 网络提供商
     */
    private String ipIsp;

    /**
     * 状态：1.通过；2.风险；3.黑名单
     */
    private Integer status;

    /**
     * 实时策略规则组ID
     */
    private Long policyDetailId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 风险识别规则列表
     */
    private String matchRules;

    /**
     * 用时(毫秒)
     */
    private Long useTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean isDelete;


}
