package cn.handsome.scim.service;

import cn.handsome.scim.model.ScimListResponse;
import cn.handsome.scim.model.ScimPatch;
import cn.handsome.scim.model.ScimUser;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public interface ScimUserService {
    ScimUser createUser(ScimUser user);

    ScimUser getUserById(String id);

    ScimUser getUserByUsername(String username);

    ScimListResponse<ScimUser> getUsers(String filter, String attributes, int startIndex, int count);

    ScimUser updateUser(String id, ScimUser user);

    ScimUser patchUser(String id, ScimPatch patch);

    void deleteUser(String id);

    ScimUser addUserToGroup(String userId, String groupId);

    ScimUser removeUserFromGroup(String userId, String groupId);
}
