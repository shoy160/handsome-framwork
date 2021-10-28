package cn.handsome.core.micro;

import cn.handsome.core.enums.ServiceProtocol;
import cn.handsome.core.lang.Weight;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/4
 */
@Getter
@Setter
public class ServiceAddress implements Weight {
    private final static String LOCAL_HOST_REG = "^(\\*|(localhost))$";
    private String host;
    private Integer port;
    /**
     * 服务协议
     */
    private ServiceProtocol protocol = ServiceProtocol.Thrift;

    /**
     * 对外注册的服务地址(ip或DNS)
     */
    private String service;
    /**
     * 对外注册的服务端口
     */
    private Integer servicePort;

    /**
     * 是否开启健康检测
     */
    private boolean healthy = true;
    /**
     * 是否瞬态
     */
    private boolean ephemeral = false;

    /**
     * 权重
     */
    private int weight = 1;

    public Integer getServicePort() {
        return (this.servicePort == null || this.servicePort <= 80) ? this.port : this.servicePort;
    }

    public ServiceAddress() {
    }

    public ServiceAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d -> %d", this.protocol, this.service, this.servicePort, this.weight).toLowerCase();
    }
}
