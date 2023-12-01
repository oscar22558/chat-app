package com.example.chatapp;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.PrivilegeJpaRepo;
import com.example.chatapp.db.repo.RoleJpaRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ChatappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatappApplication.class, args);
    }
    @Bean
    public CommandLineRunner init(
            AppUserJpaRepo userJpaRepo,
            RoleJpaRepo roleJpaRepo,
            PrivilegeJpaRepo privilegeJpaRepo,
            PasswordEncoder passwordEncoder
    ) {
        return (args) -> {
            var role = new Role();
            role.setName(RoleType.ROLE_USER);
            role.setPrivileges(new HashSet<>());
            role.setUsers(new HashSet<>());
            var savedRole = roleJpaRepo.save(role);

            var user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRoles(Set.of(savedRole));
            user.setCreateAt(AppTimestamp.newInstance());
            user = userJpaRepo.save(user);

            savedRole.getUsers().add(user);
            roleJpaRepo.save(role);
        };
    }
}
