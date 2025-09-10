package cn.handsome.scim.repository;

import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimUser;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public interface ScimUserRepository {
    ScimUser createUser(ScimUser user);

    Optional<ScimUser> getUserById(String id);

    Optional<ScimUser> getUserByUsername(String username);

    List<ScimUser> getUsers(String filter, String attributes, int startIndex, int count);

    int getUsersCount();

    ScimUser updateUser(String id, ScimUser user);

    void deleteUser(String id);

    ScimUser addGroup(String userId, String groupId, String groupName);

    ScimUser removeGroup(String userId, String groupId);

    List<ScimGroup> getGroups(String userId);
}
