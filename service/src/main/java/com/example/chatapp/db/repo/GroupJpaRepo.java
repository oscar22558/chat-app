package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJpaRepo extends JpaRepository<Group, Long> {
}
