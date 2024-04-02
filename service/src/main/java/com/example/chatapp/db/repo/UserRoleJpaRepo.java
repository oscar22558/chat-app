package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.UsersRole;
import com.example.chatapp.db.entity.UsersRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleJpaRepo extends JpaRepository<UsersRole, UsersRoleId> {
}
