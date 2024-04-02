package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupJpaRepo extends JpaRepository<Group, Long> {
    List<Group> findByIdIn(List<Long> ids);
}
