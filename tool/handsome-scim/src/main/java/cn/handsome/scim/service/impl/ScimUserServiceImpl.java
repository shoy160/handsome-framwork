package cn.handsome.scim.service.impl;

import cn.handsome.core.exception.BusinessException;
import cn.handsome.scim.model.ScimListResponse;
import cn.handsome.scim.model.ScimMember;
import cn.handsome.scim.model.ScimPatch;
import cn.handsome.scim.model.ScimUser;
import cn.handsome.scim.repository.ScimGroupRepository;
import cn.handsome.scim.repository.ScimUserRepository;
import cn.handsome.scim.service.ScimUserService;
import cn.handsome.scim.utils.ScimUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Service
@RequiredArgsConstructor
public class ScimUserServiceImpl implements ScimUserService {

    private final ScimUserRepository userRepository;
    private final ScimGroupRepository groupRepository;

    @Override
    public ScimUser createUser(ScimUser user) {
        // 验证用户名是否已存在
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            throw new BusinessException("username is required");
        }

        // 检查用户名是否已存在
        if (userRepository.getUserByUsername(user.getUserName()).isPresent()) {
            throw new BusinessException("username already exists");
        }
        return userRepository.createUser(user);
    }

    @Override
    public ScimUser getUserById(String id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));
    }

    @Override
    public ScimUser getUserByUsername(String username) {
        return userRepository.getUserByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found with username: " + username));
    }

    @Override
    public ScimListResponse<ScimUser> getUsers(String filter, String attributes, int startIndex, int count) {
        startIndex = Math.max(1, startIndex);
        count = Math.min(1000, Math.max(1, count));

        List<ScimUser> users = userRepository.getUsers(filter, attributes, startIndex, count);
        int totalResults = userRepository.getUsersCount();

        ScimListResponse<ScimUser> response = new ScimListResponse<>();
        response.setTotalResults(totalResults);
        response.setStartIndex(startIndex);
        response.setItemsPerPage(users.size());
        response.setResources(users);

        return response;
    }

    @Override
    public ScimUser updateUser(String id, ScimUser user) {
        // 检查用户是否存在
        if (!userRepository.getUserById(id).isPresent()) {
            throw new BusinessException("User not found with id: " + id);
        }
        return userRepository.updateUser(id, user);
    }

    @Override
    public ScimUser patchUser(String id, ScimPatch patch) {
        // 检查用户是否存在
        ScimUser user = getUserById(id);
        if (user == null) {
            throw new BusinessException("User not found: " + id);
        }

        for (ScimPatch.Operation op : patch.getOperations()) {
            switch (op.getOp()) {
                case "add":
                    handleAddOperation(user, op);
                    break;
                case "remove":
                    handleRemoveOperation(user, op);
                    break;
                case "replace":
                    handleReplaceOperation(user, op);
                    break;
                default:
                    throw new BusinessException("Unsupported operation: " + op.getOp());
            }
        }
        updateUser(id, user);
        return user;
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.getUserById(id).isPresent()) {
            throw new BusinessException("User not found with id: " + id);
        }
        userRepository.deleteUser(id);
    }

    @Override
    public ScimUser addUserToGroup(String userId, String groupId) {
        ScimUser user = getUserById(userId);
        String userDisplayName = user.getDisplayName() != null ? user.getDisplayName() : user.getUserName();

        groupRepository.getGroupById(groupId)
                .orElseThrow(() -> new BusinessException("Group not found:" + groupId));

        // 添加用户到群组
        groupRepository.addMember(groupId, userId, userDisplayName);

        // 同时更新用户的群组信息
        String groupName = groupRepository.getGroupById(groupId).get().getDisplayName();
        return userRepository.addGroup(userId, groupId, groupName);
    }

    @Override
    public ScimUser removeUserFromGroup(String userId, String groupId) {
        // 检查用户和群组是否存在
        getUserById(userId);
        groupRepository.getGroupById(groupId)
                .orElseThrow(() -> new BusinessException("Group not found:" + groupId));

        // 从群组移除用户
        groupRepository.removeMember(groupId, userId);

        // 同时更新用户的群组信息
        return userRepository.removeGroup(userId, groupId);
    }

    private void handleAddOperation(ScimUser user, ScimPatch.Operation op) {
        String path = op.getPath();
        if ("groups".equals(path)) {
            List<ScimMember> members = ScimUtils.convertMembers(op.getValue());
            for (ScimMember member : members) {
                addUserToGroup(user.getId(), member.getValue());
            }
        }
        if ("emails".equals(path)) {
            List<Map<String, Object>> emails = ScimUtils.convertListMap(op.getValue());
            for (Map<String, Object> emailValue : emails) {
                ScimUser.Email email = new ScimUser.Email();
                email.setValue((String) emailValue.get("value"));
                email.setType((String) emailValue.get("type"));
                email.setPrimary((Boolean) emailValue.getOrDefault("primary", false));
                user.getEmails().add(email);
                user.getMeta().changed();
            }
        }
    }

    private void handleRemoveOperation(ScimUser user, ScimPatch.Operation op) {
        if (op.getPath().startsWith("emails[value eq \"")) {
            String emailValue = op.getPath().split("\"")[1];
            user.getEmails().removeIf(email -> emailValue.equals(email.getValue()));
            user.getMeta().changed();
        } else if (op.getPath().startsWith("groups[value eq \"")) {
            String groupId = op.getPath().split("\"")[1];
            removeUserFromGroup(user.getId(), groupId);
        }
    }

    private void handleReplaceOperation(ScimUser user, ScimPatch.Operation op) {
        if ("userName".equals(op.getPath())) {
            user.setUserName((String) op.getValue());
        } else if ("displayName".equals(op.getPath())) {
            user.setDisplayName((String) op.getValue());
        } else if ("name.givenName".equals(op.getPath())) {
            if (user.getName() == null) {
                user.setName(new ScimUser.Name());
            }
            user.getName().setGivenName((String) op.getValue());
        } else if ("name.familyName".equals(op.getPath())) {
            if (user.getName() == null) {
                user.setName(new ScimUser.Name());
            }
            user.getName().setFamilyName((String) op.getValue());
        }
        user.getMeta().changed();
    }
}
