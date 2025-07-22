package cn.handsome.workflow.domain;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@Setter
public abstract class Auditable {
    private Long createTime;

    private Long updateTime;

    private String createdBy;

    private String updatedBy;
    protected Auditable() {
        this.createTime = System.currentTimeMillis();
    }
}
