package cn.handsome.core.compare.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2023/6/26
 */
@Getter
@Setter
public class NodeSyncScope {
    private final static String USER_SYNC_ALL = "all";
    private final static String USER_SYNC_NONE = "none";
    private final static String USER_SYNC_INCLUDE = "include";
    private final static String USER_SYNC_EXCLUDE = "exclude";
    
    private String depId;
    /**
     * -1，0，1 => -1是初始状态，0是部分选择，1是全部选择部门
     */
    private int syncDepType;
    /**
     * none 一个部门没选, all 选中了所有部门, partial 部分选择
     */
    private String syncSubDepType;
    /**
     * all 所有用户；none 不同步用户；include 包含部分用户；exclude 排除部分用户
     */
    private String syncUserType;
    /**
     * 选择部门，排除或包含某些用户
     */
    private List<String> users;

    public boolean isSyncUserAll() {
        return Objects.equals(USER_SYNC_ALL, this.syncUserType);
    }

    public boolean isSyncUserInclude() {
        return Objects.equals(USER_SYNC_INCLUDE, this.syncUserType);
    }

    public boolean isSyncUserExclude() {
        return Objects.equals(USER_SYNC_EXCLUDE, this.syncUserType);
    }

    public boolean isSyncUserNone() {
        return Objects.equals(USER_SYNC_NONE, this.syncUserType);
    }
}
