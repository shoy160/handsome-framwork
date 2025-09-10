package cn.handsome.scim.repository.impl;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public abstract class BaseScimRepository {

//    protected void addGroup(ScimUser user, ScimMember member) {
//        if (isEmpty(member)) {
//            return;
//        }
//        List<ScimMember> groups = user.getGroups();
//        if (groups.stream().anyMatch(t -> t.getValue().equals(member.getValue()))) {
//            return;
//        }
//        member.groupRef();
//        groups.add(member);
//    }
//
//    protected void removeGroup(ScimUser user, ScimMember member) {
//        if (isEmpty(member)) {
//            return;
//        }
//        user.getGroups().removeIf(t -> t.getValue().equals(member.getValue()));
//    }
//
//    protected void addMember(ScimGroup group, ScimMember member) {
//        if (isEmpty(member)) {
//            return;
//        }
//        List<ScimMember> members = group.getMembers();
//        String userId = member.getValue();
//        if (members.stream().anyMatch(t -> t.getValue().equals(userId))) {
//            return;
//        }
//        member.userRef();
//        members.add(member);
//    }
//
//    protected void removeMember(ScimGroup group, ScimMember member) {
//        if (isEmpty(member)) {
//            return;
//        }
//        group.getMembers().removeIf(t -> t.getValue().equals(member.getValue()));
//    }
}
