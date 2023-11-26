package com.example.chatapp.features.group;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MemberJpaRepo;
import com.example.chatapp.features.group.model.GroupPermissionChecker;
import com.example.chatapp.features.user.UserIdentityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class GroupMemberService {
    GroupJpaRepo groupJpaRepo;
    AppUserJpaRepo appUserJpaRepo;
    MemberJpaRepo memberJpaRepo;
    UserIdentityService userIdentityService;
    GroupPermissionChecker groupPermissionChecker;

    public void addMember(long groupId, long userId){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        groupPermissionChecker.hasPermission(operatorUserId, userId, group);

        var newMember = new Member();
        var user = appUserJpaRepo.findById(userId).orElseThrow(RecordNotFoundException::new);
        var groupRole = GroupRoleType.MEMBER;
        newMember.setGroup(group);
        newMember.setUser(user);
        newMember.setGroupRoleType(groupRole);
        group.addMember(newMember);
        groupJpaRepo.save(group);
        memberJpaRepo.save(newMember);
    }

    public void addMembers(long groupId, List<Long> userIds){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        groupPermissionChecker.hasPermission(operatorUserId, userIds, group);

        var groupRole = GroupRoleType.MEMBER;
        var newMembers = appUserJpaRepo.findAllById(userIds)
                .stream().map(appUser -> {
                    var newMember = new Member();
                    newMember.setGroup(group);
                    newMember.setUser(appUser);
                    newMember.setGroupRoleType(groupRole);
                    return newMember;
                }).toList();
        group.addMembers(newMembers);
        groupJpaRepo.save(group);
        memberJpaRepo.saveAll(newMembers);
    }

    public void removeMember(long groupId, long userId){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        groupPermissionChecker.hasPermission(operatorUserId, userId, group);

        var groupMember = group.getMembers().stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);
        group.removeMember(groupMember);
        memberJpaRepo.delete(groupMember);
        groupJpaRepo.save(group);
    }

    public void removeMembers(long groupId, List<Long> userIds){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        groupPermissionChecker.hasPermission(operatorUserId, userIds, group);

        var groupMembers = group.getMembers().stream()
                .filter(member ->
                        userIds.stream().anyMatch(id -> id.equals(member.getUser().getId()))
                ).toList();
        group.removeMembers(groupMembers);
        memberJpaRepo.deleteAll(groupMembers);
        groupJpaRepo.save(group);
    }


    public void leaveGroup(long id){
        //remove authed user id from member list of the group
        var userId = userIdentityService.getUserId();
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        group.getMembers().stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst()
                .ifPresent(member -> {
                    group.removeMember(member);
                    memberJpaRepo.delete(member);
                });
        groupJpaRepo.save(group);
    }
}
