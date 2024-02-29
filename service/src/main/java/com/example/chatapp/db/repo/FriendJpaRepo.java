package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendJpaRepo extends JpaRepository<Friend, Long> {
}
