package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface RoleJpaRepo extends JpaRepository<Role, Long> {
}
