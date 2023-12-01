package com.example.chatapp.features.group;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.ContactJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MemberJpaRepo;
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
    ContactJpaRepo contactJpaRepo;
    MemberPermissionChecker memberPermissionChecker;

    public void addMember(long groupId, long userId){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        memberPermissionChecker.setGroup(group);
        memberPermissionChecker.canAddMember(operatorUserId);

        var newMember = new Member();
        var user = appUserJpaRepo.findById(userId).orElseThrow(RecordNotFoundException::new);
        var groupRole = GroupRoleType.MEMBER;
        newMember.setGroup(group);
        newMember.setUser(user);
        newMember.setGroupRoleType(groupRole);
        group.addMember(newMember);
        memberJpaRepo.save(newMember);
        groupJpaRepo.save(group);

        var memberContacts = newMember.getUser().getContacts();
        var newContact = new Contact();
        newContact.setRecipientId(groupId);
        newContact.setRecipientType(RecipientType.GROUP);
        newContact.setUser(user);
        newContact.setUpdatedAt(AppTimestamp.newInstance());
        memberContacts.add(newContact);
        contactJpaRepo.save(newContact);
        //TODO: update contact subscription of newMember
    }

    public void addMembers(long groupId, List<Long> userIds){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        memberPermissionChecker.setGroup(group);
        memberPermissionChecker.canAddMember(operatorUserId);

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

        newMembers.forEach(member -> {
            var user = member.getUser();
            var memberContacts = user.getContacts();
            var newContact = new Contact();
            newContact.setRecipientId(groupId);
            newContact.setRecipientType(RecipientType.GROUP);
            newContact.setUser(user);
            newContact.setUpdatedAt(AppTimestamp.newInstance());
            memberContacts.add(newContact);
            contactJpaRepo.save(newContact);
            //TODO: update contact subscription of newMember
        });
    }

    public void removeMember(long groupId, long userId){
        var group = groupJpaRepo.findById(groupId).orElseThrow(RecordNotFoundException::new);
        var operatorUserId = userIdentityService.getUserId();
        memberPermissionChecker.setGroup(group);
        memberPermissionChecker.canRemoveMember(operatorUserId, userId);

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
        memberPermissionChecker.setGroup(group);
        memberPermissionChecker.canRemoveMembers(operatorUserId, userIds);

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
