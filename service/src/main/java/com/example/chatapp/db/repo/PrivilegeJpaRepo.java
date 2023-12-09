package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeJpaRepo extends JpaRepository<Privilege, Long> {
}
