package com.example.chatapp;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.*;
import com.example.chatapp.redis.ContactRedisRepo;
import com.example.chatapp.redis.entity.Contact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DataSeed implements CommandLineRunner {
    final AppUserJpaRepo userJpaRepo;
    final RoleJpaRepo roleJpaRepo;
    final FriendJpaRepo friendJpaRepo;
    final PasswordEncoder passwordEncoder;
    final ObjectMapper objectMapper;
    final ContactRedisRepo contactRedisRepo;
    final RedisTemplate<String, Object> redisTemplate;

    final GroupJpaRepo groupJpaRepo;
    final MemberJpaRepo memberJpaRepo;
    final MessageJpaRepo messageJpaRepo;
    final PrivilegeJpaRepo privilegeJpaRepo;
    final UserRoleJpaRepo userRoleJpaRepo;
    final RolesPrivilegeJpaRepo rolesPrivilegeJpaRepo;
    final GrpMsgReadStatusRepo grpMsgReadStatusRepo;

    @Value("classpath:data/testing-data.json")
    Resource resourceFile;

    @Data
    public static class JsonData {
        Role role;
        List<AppUser> users;
        List<List<String>> friendPairs;
        List<List<String>> contacts;
        Map<String, Map<String, List<String>>> groups;
    }

    @Override
    public void run(String... args) {
        try {
            log.info("Starting data seed...");
            boolean resetDb = false;
            boolean updateRedis = false;
            var jsonData = objectMapper.readValue(resourceFile.getFile(), JsonData.class);
            var savedUsers = new ArrayList<AppUser>();
            var savedFriends = new ArrayList<Friend>();
            var savedGroups = new ArrayList<Group>();
            if (resetDb) {
                insertToDb(jsonData, savedUsers, savedFriends, savedGroups);
            }

            if (updateRedis) {
                if (!resetDb) {
                    savedUsers.addAll(userJpaRepo.findAll());
                    savedFriends.addAll(friendJpaRepo.findAll());
                    savedGroups.addAll(groupJpaRepo.findAll());
                }
                updateRedis(jsonData, savedUsers, savedFriends, savedGroups);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            log.info("Data seed ended!!");
        }

    }

    public void insertToDb(JsonData jsonData, List<AppUser> savedUsersOutput, List<Friend> friendsOutput, List<Group> groupsOutput) {
        grpMsgReadStatusRepo.deleteAll();
        friendJpaRepo.deleteAll();
        memberJpaRepo.deleteAll();
        groupJpaRepo.deleteAll();
        messageJpaRepo.deleteAll();
        userRoleJpaRepo.deleteAll();
        userJpaRepo.deleteAll();
        rolesPrivilegeJpaRepo.deleteAll();
        privilegeJpaRepo.deleteAll();
        roleJpaRepo.deleteAll();

        var role = jsonData.getRole();
        var savedRole = roleJpaRepo.save(role);
        jsonData.getUsers().forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Set.of(savedRole));
            user.setCreateAt(AppTimestamp.newInstance());
            user.setGroupMembers(new ArrayList<>());
            user.setSentMessages(new ArrayList<>());
            user.setReceivedFriendRequests(new ArrayList<>());
            user.setSentFriendRequests(new ArrayList<>());
        });

        var savedUsers = userJpaRepo.saveAll(jsonData.getUsers());
        savedUsersOutput.addAll(savedUsers);
        savedRole.getUsers().addAll(savedUsers);

        var friends = jsonData.getFriendPairs()
                .stream()
                .map(pair -> {
                    var user = findUserByUsername(savedUsers, pair.get(0));
                    var target = findUserByUsername(savedUsers, pair.get(1));
                    var friend = new Friend();
                    friend.setRequestSender(user);
                    friend.setRequestReceiver(target);
                    friend.setCreateAt(AppTimestamp.newInstance());
                    friend.setStatus(FriendStatus.ACCEPTED);
                    friendJpaRepo.save(friend);
                    user.getSentFriendRequests().add(friend);
                    target.getReceivedFriendRequests().add(friend);
                    return friend;
                })
                .toList();
        friendsOutput.addAll(friends);

        var groups = jsonData.getGroups()
                .keySet()
                .stream()
                .map(strings -> {
                    var grp = new Group();
                    grp.setName(strings);
                    grp.setMembers(new ArrayList<>());
                    grp.setIsDeleted(false);
                    return grp;
                })
                .toList();
        groupJpaRepo.saveAll(groups);
        groupsOutput.addAll(groups);

        var members = jsonData.getGroups()
                .entrySet()
                .stream()
                .flatMap((entry) -> {
                    var grp = groups.stream().filter(savedGrp -> savedGrp.getName().equals(entry.getKey())).findFirst().get();
                    return entry
                            .getValue()
                            .entrySet()
                            .stream()
                            .flatMap(grpUsrEntry -> {
                                var memberType = GroupRoleType.valueOf(grpUsrEntry.getKey());
                                var usernames = grpUsrEntry.getValue();
                                return usernames.stream().map(username -> {
                                   var user = findUserByUsername(savedUsers, username);
                                    var member = new Member();
                                    member.setGroup(grp);
                                    member.setInvitationStatus(MemberInvitationStatus.ACCEPTED);
                                    member.setGroupRoleType(memberType);
                                    member.setUser(user);
                                    return member;
                                });
                            });
                })
                .toList();
        memberJpaRepo.saveAll(members);

    }

    public void updateRedis(JsonData jsonData, List<AppUser> savedUsers, List<Friend> friends, List<Group> groups) {
        contactRedisRepo.deleteAll();
        friends
                .forEach(frd -> {
                    var sender = findUserByUsername(savedUsers, frd.getRequestSender().getUsername());
                    var recipient = findUserByUsername(savedUsers, frd.getRequestReceiver().getUsername());
                    var senderContact = buildContact(sender, recipient);
                    var recipientContact = buildContact(recipient, sender);
                    contactRedisRepo.save(senderContact);
                    contactRedisRepo.save(recipientContact);
                });
        groups.forEach(group -> {
            var members = memberJpaRepo.findByGroupId(group.getId());
            var userIds = members.stream().map(member -> member.getUser().getId()).toList();
            var users = userJpaRepo.findByIdIn(userIds);
            var contacts = users.stream()
                    .map(user -> {
                        var contact = new Contact();
                        contact.setNewMsgCount(0);

                        var grpMsgStatuses = grpMsgReadStatusRepo.findByGrpIdAndGrpUsrId(group.getId(), user.getId());
                        contact.setMsgCount(grpMsgStatuses.size());
                        contact.setReadMsgCount(grpMsgStatuses.stream().filter(grpMsgStatus -> grpMsgStatus.getReadAt() != null).toList().size());
                        contact.setUpdatedAt(AppTimestamp.newInstance());
                        contact.setRecipientId(group.getId());
                        contact.setRecipientType(RecipientType.GROUP);
                        contact.setUserId(user.getId());
                        return contact;
                    })
                    .toList();
            contactRedisRepo.saveAll(contacts);
        });
    }

    private AppUser findUserByUsername(List<AppUser> savedUsers, String username) {
        return savedUsers.stream()
                .filter(savedUser -> savedUser.getUsername().equals(username))
                .findFirst()
                .orElseThrow(InvalidParameterException::new);
    }

    private Contact buildContact(AppUser sender, AppUser recipient) {
        var contact = new Contact();
        contact.setNewMsgCount(0);
        contact.setMsgCount(0);
        contact.setReadMsgCount(0);
        contact.setUpdatedAt(AppTimestamp.newInstance());
        contact.setRecipientId(recipient.getId());
        contact.setRecipientType(RecipientType.USER);
        contact.setUserId(sender.getId());
        return contact;
    }

}
