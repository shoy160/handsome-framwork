package cn.handsome.scim.service;

import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimListResponse;
import cn.handsome.scim.model.ScimPatch;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public interface ScimGroupService {
    ScimGroup createGroup(ScimGroup group);

    ScimGroup getGroupById(String id);

    ScimListResponse<ScimGroup> getGroups(String filter, String attributes, int startIndex, int count);

    ScimGroup updateGroup(String id, ScimGroup group);

    ScimGroup patchGroup(String id, ScimPatch patch);

    ScimGroup addMemberToGroup(String groupId, String userId);

    ScimGroup removeMemberFromGroup(String groupId, String userId);

    ScimListResponse<ScimGroup> getGroupsForUser(String userId);

    void deleteGroup(String id);
}
