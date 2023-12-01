package com.example.chatapp.features.group;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.common.exception.UnAuthorizedException;
import com.example.chatapp.db.entity.Contact;
import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MemberJpaRepo;
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

        members.forEach(member -> {
            var contacts = member.getUser().getContacts();
            var newContact = new Contact();
            newContact.setRecipientId(savedGroup.getId());
            newContact.setRecipientType(RecipientType.GROUP);
            newContact.setUpdatedAt(AppTimestamp.newInstance());
            newContact.setUser(member.getUser());
            contacts.add(newContact);
        });
        //TODO: update contact subscriptions

        return groupJpaRepo.save(group).getId();
    }

    public void updateGroup(long id, GroupPutRequest request){
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        group.setName(request.getName());
        groupJpaRepo.save(group);
    }

    public void deleteGroup(long id){
        var group = groupJpaRepo.findById(id).orElseThrow(RecordNotFoundException::new);
        var members = group.getMembers();
        memberJpaRepo.deleteAll(members);
        groupJpaRepo.deleteById(id);
    }
}
