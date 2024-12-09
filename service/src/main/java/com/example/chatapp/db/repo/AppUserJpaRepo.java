package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserJpaRepo extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByUsername(String username);
    List<AppUser> findAppUsersByUsernameContainsIgnoreCase(String username);
    List<AppUser> findByIdIn(List<Long> ids);
}
