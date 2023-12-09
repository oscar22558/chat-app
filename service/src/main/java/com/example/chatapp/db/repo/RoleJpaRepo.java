package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepo extends JpaRepository<Role, Long> {
}
