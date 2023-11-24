package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserJpaRepo extends JpaRepository<AppUser, Long> {
}
