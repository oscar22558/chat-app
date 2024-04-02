package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberJpaRepo extends JpaRepository<Member, Long> {
    List<Member> findByGroupIdAndUserId(long groupId, long usrId);
    List<Member> findByGroupId(long groupId);
    List<Member> findByGroupIdIn(List<Long> groupId);
}
