package cn.handsome.sap.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyong
 * @date 2025/4/28
 */
@Getter
@Setter
@EqualsAndHashCode
public class SapRfcCredentials {
    private String host;
    private String port;
    /**
     * SAP 用户名
     */
    private String username;
    /**
     * SAP 密码
     */
    private String password;
    /**
     * 系统编号
     */
    private String systemNumber;
    private String group;
    /**
     * 登录语言
     */
    private String lang;
    /**
     * SAP 集团
     */
    private String client;
    /**
     * 最大连接数
     */
    private Integer poolCapacity;
    /**
     * 最大连接线程
     */
    private Integer peakLimit;
    private String clientId;
    private String clientSecret;

    public SapRfcCredentials() {
        this.lang = "ZH";
        this.poolCapacity = 3;
        this.peakLimit = 10;
    }

    public String getPoolName() {
        return String.format("sap_%s_pool", this.hashCode());
    }
}
