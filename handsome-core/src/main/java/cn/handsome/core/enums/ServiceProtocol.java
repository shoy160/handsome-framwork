package cn.handsome.core.enums;

/**
 * 服务协议
 *
 * @author shoy
 * @date 2021/6/4
 */
public enum ServiceProtocol implements BaseNamedEnum {
    /**
     * Thrift
     */
    Thrift(1, "Thrift"),
    ;
    private final int value;
    private final String name;

    ServiceProtocol(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
