package com.example.chatapp;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Role;
import com.example.chatapp.db.entity.RoleType;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.RoleJpaRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitializeDB {
    final AppUserJpaRepo userJpaRepo;
    final RoleJpaRepo roleJpaRepo;
    final PasswordEncoder passwordEncoder;
    final ObjectMapper objectMapper;

    @Value("classpath:data/testing-data.json")
    Resource resourceFile;

    @Data
    private static class JsonData{
        Role role;
        List<AppUser> users;

    }
    public InitializeDB(AppUserJpaRepo userJpaRepo, RoleJpaRepo roleJpaRepo, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userJpaRepo = userJpaRepo;
        this.roleJpaRepo = roleJpaRepo;
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
            });

            var savedUsers = userJpaRepo.saveAll(jsonData.getUsers());
            savedRole.getUsers().addAll(savedUsers);
            roleJpaRepo.save(role);
        };
    }
}
