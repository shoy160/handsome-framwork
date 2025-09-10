package cn.handsome.core.domain;

import java.util.Date;

/**
 * 拥有时间属性
 *
 * @author shay
 * @date 2021/3/5
 */
public interface HaveDate {
    /**
     * 获取创建时间
     *
     * @return date
     */
    Date getCreateTime();

    /**
     * 设置创建时间
     *
     * @param createTime create time
     */
    void setCreateTime(Date createTime);

    /**
     * 获取更新时间
     *
     * @return Date
     */
    Date getUpdateTime();

    /**
     * 设置更新时间
     *
     * @param updateTime update time
     */
    void setUpdateTime(Date updateTime);
}
