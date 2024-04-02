package com.example.chatapp;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.ContactJpaRepo;
import com.example.chatapp.db.repo.FriendJpaRepo;
import com.example.chatapp.db.repo.RoleJpaRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSeed {
    final AppUserJpaRepo userJpaRepo;
    final RoleJpaRepo roleJpaRepo;
    final FriendJpaRepo friendJpaRepo;
    final PasswordEncoder passwordEncoder;
    final ObjectMapper objectMapper;
    final ContactJpaRepo contactJpaRepo;

    @Value("classpath:data/testing-data.json")
    Resource resourceFile;

    @Data
    private static class JsonData{
        Role role;
        List<AppUser> users;
        List<List<String>> friendPairs;
        List<List<String>> contacts;
    }

    public DataSeed(AppUserJpaRepo userJpaRepo, RoleJpaRepo roleJpaRepo, FriendJpaRepo friendJpaRepo, PasswordEncoder passwordEncoder, ObjectMapper objectMapper, ContactJpaRepo contactJpaRepo) {
        this.userJpaRepo = userJpaRepo;
        this.roleJpaRepo = roleJpaRepo;
        this.friendJpaRepo = friendJpaRepo;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.contactJpaRepo = contactJpaRepo;
    }

    public CommandLineRunner getRunner(){
        return (args) -> {
            var jsonData = objectMapper.readValue(resourceFile.getFile(), JsonData.class);
            var role = jsonData.getRole();
            var savedRole = roleJpaRepo.save(role);
            jsonData.getUsers().forEach(user -> {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRoles(Set.of(savedRole));
                user.setCreateAt(AppTimestamp.newInstance());
                user.setContacts(new ArrayList<>());
                user.setGroupMembers(new ArrayList<>());
                user.setSentMessages(new ArrayList<>());
                user.setReceivedFriendRequests(new ArrayList<>());
                user.setSentFriendRequests(new ArrayList<>());
            });

            var savedUsers = userJpaRepo.saveAll(jsonData.getUsers());
            savedRole.getUsers().addAll(savedUsers);
            roleJpaRepo.save(role);

            jsonData.getFriendPairs()
                    .forEach(pair -> {
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
                    });

            jsonData.getContacts()
                    .forEach(contact -> {
                        var sender = findUserByUsername(savedUsers, contact.get(0));
                        var recipient = findUserByUsername(savedUsers, contact.get(1));
                        var senderContact = buildContact(sender, recipient);
                        var recipientContact = buildContact(recipient, sender);
                        contactJpaRepo.save(senderContact);
                        contactJpaRepo.save(recipientContact);
                    });
        };
    }

    private AppUser findUserByUsername(List<AppUser> savedUsers, String username){
        return savedUsers.stream()
                .filter(savedUser -> savedUser.getUsername().equals(username))
                .findFirst()
                .orElseThrow(InvalidParameterException::new);
    }

    private Contact buildContact(AppUser sender, AppUser recipient){
        var contact = new Contact();
        contact.setNewMsgCount(0);
        contact.setUpdatedAt(AppTimestamp.newInstance());
        contact.setRecipientId(recipient.getId());
        contact.setRecipientType(RecipientType.USER);
        contact.setUser(sender);
        return contact;
    }

}
