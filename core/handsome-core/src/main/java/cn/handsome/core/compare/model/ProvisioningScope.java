package cn.handsome.core.compare.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author luoyong
 * @date 2023/6/26
 */
@Getter
@Setter
public class ProvisioningScope {
    /**
     * 实际上是部门 ID
     */
    private String orgId;
    private String orgName;
    private boolean all;
    private List<NodeSyncScope> nodeSyncScope;
}
