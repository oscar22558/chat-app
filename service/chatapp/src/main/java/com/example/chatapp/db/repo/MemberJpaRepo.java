package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepo extends JpaRepository<Member, Long> {
}
