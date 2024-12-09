package com.example.chatapp.features.group;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.entity.MemberInvitationStatus;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MemberJpaRepo;
import com.example.chatapp.features.contact.ContactService;
import com.example.chatapp.features.contact.ContactUpdatePushService;
import com.example.chatapp.converter.GroupCreateRequestDTOConverter;
import com.example.chatapp.converter.GroupDTOConverter;
import com.example.chatapp.features.group.model.GroupCreateRequest;
import com.example.chatapp.features.group.model.GroupPutRequest;
import com.example.chatapp.model.GroupDTO;
import com.example.chatapp.features.user.UserIdentityService;
import com.example.chatapp.model.MemberInvitationStatusTransaction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

public class GroupService {

    GroupJpaRepo groupJpaRepo;
    AppUserJpaRepo userJpaRepo;
    MemberJpaRepo memberJpaRepo;
    UserIdentityService userIdentityService;
    GroupCreateRequestDTOConverter groupCreateRequestDTOConverter = new GroupCreateRequestDTOConverter();
    GroupDTOConverter groupDTOConverter;
    MemberPermissionChecker memberPermissionChecker;
    ContactUpdatePushService contactUpdatePushService;
    ContactService contactService;

    public GroupDTO getGroup(long id){
        var authUserId = userIdentityService.getUserId();
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        memberPermissionChecker.setGroup(group);
        memberPermissionChecker.canGetGroup(authUserId);
        return groupDTOConverter.map(group);
    }

    public long createGroup(GroupCreateRequest request){
        var authedUserId = userIdentityService.getUserId();

        var memberUserIdSet = new HashSet<>(request.getMemberUserIds());
        memberUserIdSet.add(authedUserId);
        var memberUserIdList = memberUserIdSet.stream().toList();
        var group = groupCreateRequestDTOConverter.map(request);
        var savedGroup = groupJpaRepo.save(group);
        var members = userJpaRepo.findAllById(memberUserIdList)
                .stream().map(appUser -> {
                    var member = new Member();
                    var groupRole = Objects.equals(appUser.getId(), authedUserId) ? GroupRoleType.ADMIN : GroupRoleType.MEMBER;
                    var invitationStatus =
                            Objects.equals(appUser.getId(), authedUserId)
                                    ? MemberInvitationStatus.ACCEPTED
                                    : MemberInvitationStatus.PENDING;
                    member.setGroupRoleType(groupRole);
                    member.setUser(appUser);
                    member.setGroup(savedGroup);
                    member.setInvitationStatus(invitationStatus);
                    return member;
                }).toList();
        members.forEach(savedGroup::addMember);
        memberJpaRepo.saveAll(members);
        var groupId = groupJpaRepo.save(group).getId();

        members.forEach(member -> contactService.addContact(member.getUser(), savedGroup));
        return groupId;
    }

    public void updateGroupName(long id, GroupPutRequest request){
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        group.setName(request.getName());
        var savedGroup = groupJpaRepo.save(group);

        savedGroup.getMembers().stream().map(Member::getUser)
                .forEach(contactUpdatePushService::push);

    }

    public void deleteGroup(long id){
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        group.setIsDeleted(true);
        groupJpaRepo.save(group);
        var members = group.getMembers();
//        var users = members.stream().map(Member::getUser);
//        memberJpaRepo.deleteAll(members);

        members.stream().map(Member::getUser)
            .forEach(user -> {
                contactService.removeContact(user, group);
                contactUpdatePushService.push(user);
            });
    }

    public void acceptGroupInvitation(Long id){
        var authUserId = userIdentityService.getUserId();
        var member = memberJpaRepo.findByGroupIdAndUserId(id, authUserId)
                .stream()
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);
        new MemberInvitationStatusTransaction().acceptInvitation(member);
        memberJpaRepo.save(member);
    }

    public void rejectGroupInvitation(Long id){
        var authUserId = userIdentityService.getUserId();
        var member = memberJpaRepo.findByGroupIdAndUserId(id, authUserId)
                .stream()
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);
        new MemberInvitationStatusTransaction().rejectInvitation(member);
        memberJpaRepo.save(member);
    }
}
