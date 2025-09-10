package cn.handsome.scim.repository.impl;

import cn.handsome.scim.model.ScimFilter;
import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimMember;
import cn.handsome.scim.model.ScimMeta;
import cn.handsome.scim.model.ScimUser;
import cn.handsome.scim.repository.ScimUserRepository;
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
import java.util.stream.Collectors;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Repository
public class InMemoryScimUserRepository extends BaseScimRepository implements ScimUserRepository {
    private final Map<String, ScimUser> users = new ConcurrentHashMap<>();

    @Override
    public ScimUser createUser(ScimUser user) {
        String id = user.getId();
        users.put(id, user);
        return user;
    }

    @Override
    public Optional<ScimUser> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<ScimUser> getUserByUsername(String username) {
        return users.values().stream()
                .filter(user -> username.equals(user.getUserName()))
                .findFirst();
    }

    @Override
    public List<ScimUser> getUsers(String filter, String attributes, int startIndex, int count) {
        List<ScimUser> userList = new ArrayList<>(users.values());
        ScimFilter scimFilter = FilterParser.parse(filter);
        userList = FilterEvaluator.filterUsers(userList, scimFilter);
        int fromIndex = Math.max(0, startIndex - 1);
        int toIndex = Math.min(fromIndex + count, userList.size());
        List<ScimUser> users = userList.subList(fromIndex, toIndex);
        List<String> attrs = StrUtil.split(attributes, ",");
        if (!CollUtil.contains(attrs, "groups")) {
            users.forEach(u -> u.setGroups(null));
        }
        return users;
    }

    @Override
    public int getUsersCount() {
        return users.size();
    }

    @Override
    public ScimUser updateUser(String id, ScimUser user) {
        Optional<ScimUser> userOp = getUserById(id);
        userOp.orElseThrow(IllegalArgumentException::new);
        ScimMeta meta = userOp.get().getMeta().changed();
        user.setId(id);
        user.setMeta(meta);
        users.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(String id) {
        users.remove(id);
    }

    @Override
    public ScimUser addGroup(String userId, String groupId, String groupName) {
        ScimUser user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        ScimMember groupRef = ScimMember.group(groupId, groupName);
        // 避免重复添加
        if (user.getGroups().stream().noneMatch(g -> g.getValue().equals(groupId))) {
            user.getGroups().add(groupRef);
        }

        return user;
    }

    @Override
    public ScimUser removeGroup(String userId, String groupId) {
        ScimUser user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        user.getGroups().removeIf(group -> group.getValue().equals(groupId));
        return user;
    }

    @Override
    public List<ScimGroup> getGroups(String userId) {
        Optional<ScimUser> userOp = getUserById(userId);
        userOp.orElseThrow(IllegalArgumentException::new);
        List<ScimMember> groups = userOp.get().getGroups();
        return groups.stream().map(t -> {
            ScimGroup group = new ScimGroup();
            group.setId(t.getValue());
            group.setDisplayName(t.getDisplay());
            return group;
        }).collect(Collectors.toList());
    }
}
