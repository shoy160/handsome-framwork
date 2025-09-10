package cn.handsome.scim.repository.impl;

import cn.handsome.scim.model.ScimFilter;
import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimMember;
import cn.handsome.scim.model.ScimMeta;
import cn.handsome.scim.repository.ScimGroupRepository;
import cn.handsome.scim.utils.FilterEvaluator;
import cn.handsome.scim.utils.FilterParser;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Repository
public class InMemoryScimGroupRepository extends BaseScimRepository implements ScimGroupRepository {
    private final Map<String, ScimGroup> groups = new ConcurrentHashMap<>();

    @Override
    public ScimGroup createGroup(ScimGroup group) {
        String id = group.getId();
        groups.put(id, group);
        return group;
    }

    @Override
    public Optional<ScimGroup> getGroupById(String id) {
        return Optional.ofNullable(groups.get(id));
    }

    @Override
    public List<ScimGroup> getGroups(String filter, String attributes, int startIndex, int count) {
        List<ScimGroup> groupList = new ArrayList<>(groups.values());
        ScimFilter scimFilter = FilterParser.parse(filter);
        groupList = FilterEvaluator.filterGroups(groupList, scimFilter);
        int fromIndex = Math.max(0, startIndex - 1);
        int toIndex = Math.min(fromIndex + count, groupList.size());
        List<ScimGroup> groups = groupList.subList(fromIndex, toIndex);
        List<String> attrs = StrUtil.split(attributes, ",");
        if (!CollUtil.contains(attrs, "members")) {
            groups.forEach(u -> u.setMembers(null));
        }
        return groups;
    }

    @Override
    public int getGroupsCount() {
        return groups.size();
    }

    @Override
    public ScimGroup updateGroup(String id, ScimGroup group) {
        Optional<ScimGroup> groupOptional = getGroupById(id);
        groupOptional.orElseThrow(IllegalArgumentException::new);
        ScimMeta meta = groupOptional.get().getMeta();
        meta.changed();
        group.setId(id);
        group.setMeta(meta);
        groups.put(id, group);
        return group;
    }

    @Override
    public void deleteGroup(String id) {
        groups.remove(id);
    }

    @Override
    public ScimGroup addMember(String groupId, String userId, String userDisplayName) {
        ScimGroup group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }

        ScimMember member = ScimMember.user(userId, userDisplayName);
        // 避免重复添加
        if (group.getMembers().stream().noneMatch(m -> m.getValue().equals(userId))) {
            group.getMembers().add(member);
        }

        return group;
    }

    @Override
    public ScimGroup removeMember(String groupId, String userId) {
        ScimGroup group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        group.getMembers().removeIf(member -> member.getValue().equals(userId));
        return group;
    }
}
