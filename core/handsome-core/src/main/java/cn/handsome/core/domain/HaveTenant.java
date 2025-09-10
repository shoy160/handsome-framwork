package cn.handsome.core.domain;

/**
 * 拥有租户属性
 *
 * @author shay
 * @date 2021/3/5
 */
public interface HaveTenant<T> {
    /**
     * 获取租户ID
     *
     * @return T
     */
    T getTenantId();

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    void setTenantId(T tenantId);
}
