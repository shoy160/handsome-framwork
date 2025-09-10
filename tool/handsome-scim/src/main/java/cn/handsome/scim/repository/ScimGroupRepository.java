package cn.handsome.scim.repository;

import cn.handsome.scim.model.ScimGroup;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public interface ScimGroupRepository {
    ScimGroup createGroup(ScimGroup group);

    Optional<ScimGroup> getGroupById(String id);

    List<ScimGroup> getGroups(String filter, String attributes, int startIndex, int count);

    int getGroupsCount();

    ScimGroup updateGroup(String id, ScimGroup group);

    void deleteGroup(String id);

    ScimGroup addMember(String groupId, String userId, String userDisplayName);

    ScimGroup removeMember(String groupId, String userId);
}
