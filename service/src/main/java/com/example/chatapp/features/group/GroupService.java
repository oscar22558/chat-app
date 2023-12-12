package com.example.chatapp.features.group;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MemberJpaRepo;
import com.example.chatapp.features.contact.ContactService;
import com.example.chatapp.features.contact.ContactUpdatePushService;
import com.example.chatapp.features.group.mapper.GroupCreateRequestMapper;
import com.example.chatapp.features.group.mapper.GroupViewMapper;
import com.example.chatapp.features.group.model.GroupCreateRequest;
import com.example.chatapp.features.group.model.GroupPutRequest;
import com.example.chatapp.features.group.model.GroupView;
import com.example.chatapp.features.user.UserIdentityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class GroupService {

    GroupJpaRepo groupJpaRepo;
    AppUserJpaRepo userJpaRepo;
    MemberJpaRepo memberJpaRepo;
    UserIdentityService userIdentityService;
    GroupCreateRequestMapper groupCreateRequestMapper = new GroupCreateRequestMapper();
    GroupViewMapper groupViewMapper = new GroupViewMapper();
    MemberPermissionChecker memberPermissionChecker;
    ContactUpdatePushService contactUpdatePushService;
    ContactService contactService;

    public GroupView getGroup(long id){
        var authUserId = userIdentityService.getUserId();
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        memberPermissionChecker.setGroup(group);
        memberPermissionChecker.canGetGroup(authUserId);
        return groupViewMapper.map(group);
    }

    public long createGroup(GroupCreateRequest request){
        var authedUserId = userIdentityService.getUserId();

        var group = groupCreateRequestMapper.map(request);
        var savedGroup = groupJpaRepo.save(group);
        var members = userJpaRepo.findAllById(request.getMemberUserIds())
                .stream().map(appUser -> {
                    var member = new Member();
                    var groupRole = Objects.equals(appUser.getId(), authedUserId) ? GroupRoleType.ADMIN : GroupRoleType.MEMBER;
                    member.setGroupRoleType(groupRole);
                    member.setUser(appUser);
                    member.setGroup(savedGroup);
                    return member;
                }).toList();
        members.forEach(savedGroup::addMember);
        memberJpaRepo.saveAll(members);
        var groupId = groupJpaRepo.save(group).getId();

        members.forEach(member -> {
            contactService.addContact(member.getUser(), savedGroup);
            contactUpdatePushService.push(member.getUser());
        });

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
        var members = group.getMembers();
        var users = members.stream().map(Member::getUser);
        memberJpaRepo.deleteAll(members);

        users.forEach(user -> {
            contactService.removeContact(user, group);
            contactUpdatePushService.push(user);
        });
    }
}