package com.example.chatapp;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Friend;
import com.example.chatapp.db.entity.FriendStatus;
import com.example.chatapp.db.entity.Role;
import com.example.chatapp.db.repo.AppUserJpaRepo;
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
public class InitializeDB {
    final AppUserJpaRepo userJpaRepo;
    final RoleJpaRepo roleJpaRepo;
    final FriendJpaRepo friendJpaRepo;
    final PasswordEncoder passwordEncoder;
    final ObjectMapper objectMapper;

    @Value("classpath:data/testing-data.json")
    Resource resourceFile;

    @Data
    private static class JsonData{
        Role role;
        List<AppUser> users;
        List<List<String>> friendPairs;
    }
    public InitializeDB(AppUserJpaRepo userJpaRepo, RoleJpaRepo roleJpaRepo, FriendJpaRepo friendJpaRepo, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userJpaRepo = userJpaRepo;
        this.roleJpaRepo = roleJpaRepo;
        this.friendJpaRepo = friendJpaRepo;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
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
        };
    }

    private AppUser findUserByUsername(List<AppUser> users, String username){
        return users.stream()
                .filter(savedUser -> savedUser.getUsername().equals(username))
                .findFirst()
                .orElseThrow(InvalidParameterException::new);
    }
}
