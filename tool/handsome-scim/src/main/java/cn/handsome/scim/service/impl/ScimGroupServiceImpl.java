package cn.handsome.scim.service.impl;

import cn.handsome.core.exception.BusinessException;
import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimListResponse;
import cn.handsome.scim.model.ScimMember;
import cn.handsome.scim.model.ScimPatch;
import cn.handsome.scim.model.ScimUser;
import cn.handsome.scim.repository.ScimGroupRepository;
import cn.handsome.scim.repository.ScimUserRepository;
import cn.handsome.scim.service.ScimGroupService;
import cn.handsome.scim.utils.ScimUtils;
import cn.hutool.core.util.StrUtil;
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
public class ScimGroupServiceImpl implements ScimGroupService {

    private final ScimGroupRepository groupRepository;
    private final ScimUserRepository userRepository;

    @Override
    public ScimGroup createGroup(ScimGroup group) {
        if (group.getDisplayName() == null || group.getDisplayName().isEmpty()) {
            throw new BusinessException("displayName is required");
        }
        return groupRepository.createGroup(group);
    }

    @Override
    public ScimGroup getGroupById(String id) {
        return groupRepository.getGroupById(id)
                .orElseThrow(() -> new BusinessException("Group not found: " + id));
    }

    @Override
    public ScimListResponse<ScimGroup> getGroups(String filter, String attributes, int startIndex, int count) {
        // todo
        startIndex = Math.max(1, startIndex);
        count = Math.min(1000, Math.max(1, count));

        List<ScimGroup> groups = groupRepository.getGroups(filter, attributes, startIndex, count);
        ScimListResponse<ScimGroup> response = new ScimListResponse<>();
        response.setResources(groups);
        response.setStartIndex(startIndex);
        response.setItemsPerPage(groups.size());
        response.setTotalResults(groups.size());

        return response;
    }

    @Override
    public ScimGroup updateGroup(String id, ScimGroup group) {
        // 检查群组是否存在
        getGroupById(id);
        return groupRepository.updateGroup(id, group);
    }

    @Override
    public ScimGroup patchGroup(String id, ScimPatch patch) {
        // 检查群组是否存在
        ScimGroup group = getGroupById(id);
        if (group == null) {
            throw new BusinessException("Group not found: " + id);
        }

        for (ScimPatch.Operation op : patch.getOperations()) {
            switch (op.getOp()) {
                case "add":
                    handleAddOperation(group, op);
                    break;
                case "remove":
                    handleRemoveOperation(group, op);
                    break;
                case "replace":
                    handleReplaceOperation(group, op);
                    break;
                default:
                    throw new BusinessException("Unsupported operation: " + op.getOp());
            }
        }
        updateGroup(id, group);
        return group;
    }

    @Override
    public void deleteGroup(String id) {
        // 检查群组是否存在
        getGroupById(id);
        groupRepository.deleteGroup(id);
    }

    @Override
    public ScimGroup addMemberToGroup(String groupId, String userId) {
        // 检查群组和用户是否存在
        ScimGroup group = getGroupById(groupId);
        ScimUser user = userRepository.getUserById(userId)
                .orElseThrow(() -> new BusinessException("User not found: " + userId));

        String userDisplayName = user.getDisplayName() != null ? user.getDisplayName() : user.getUserName();

        // 添加成员到群组
        ScimGroup updatedGroup = groupRepository.addMember(groupId, userId, userDisplayName);

        // 同时更新用户的群组信息
        userRepository.addGroup(userId, groupId, group.getDisplayName());

        return updatedGroup;
    }

    @Override
    public ScimGroup removeMemberFromGroup(String groupId, String userId) {
        // 检查群组和用户是否存在
        getGroupById(groupId);
        userRepository.getUserById(userId)
                .orElseThrow(() -> new BusinessException("User not found: " + userId));

        // 从群组移除成员
        ScimGroup updatedGroup = groupRepository.removeMember(groupId, userId);

        // 同时更新用户的群组信息
        userRepository.removeGroup(userId, groupId);

        return updatedGroup;
    }

    @Override
    public ScimListResponse<ScimGroup> getGroupsForUser(String userId) {
        // 检查用户是否存在
        userRepository.getUserById(userId)
                .orElseThrow(() -> new BusinessException("User not found: " + userId));

        List<ScimGroup> groups = userRepository.getGroups(userId);
        ScimListResponse<ScimGroup> response = new ScimListResponse<>();
        response.setResources(groups);
        response.setStartIndex(1);
        response.setItemsPerPage(groups.size());
        response.setTotalResults(groups.size());
        return response;
    }

    private void handleAddOperation(ScimGroup group, ScimPatch.Operation op) {
        Object value = op.getValue();
        if ("members".equals(op.getPath())) {
            List<ScimMember> members = ScimUtils.convertMembers(value);
            for (ScimMember member : members) {
                addMemberToGroup(group.getId(), member.getValue());
            }
        } else if (op.getPath() == null && value instanceof Map) {
            Map<String, Object> values = (Map<String, Object>) value;
            if (values.containsKey("displayName")) {
                group.setDisplayName((String) values.get("displayName"));
                group.getMeta().changed();
            }
        }
    }

    private void handleRemoveOperation(ScimGroup group, ScimPatch.Operation op) {
        String path = op.getPath();
        if (StrUtil.equalsIgnoreCase("members", path)) {
            List<ScimMember> members = ScimUtils.convertMembers(op.getValue());
            for (ScimMember member : members) {
                removeMemberFromGroup(group.getId(), member.getValue());
            }
        }
        if (path.startsWith("members[value eq \"")) {
            String userId = path.split("\"")[1];
            removeMemberFromGroup(group.getId(), userId);
        }
    }

    private void handleReplaceOperation(ScimGroup group, ScimPatch.Operation op) {
        if ("displayName".equals(op.getPath())) {
            group.setDisplayName((String) op.getValue());
            group.getMeta().changed();
        }
    }
}
