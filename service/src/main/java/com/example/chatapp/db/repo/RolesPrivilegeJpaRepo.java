package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.RolesPrivilege;
import com.example.chatapp.db.entity.RolesPrivilegeId;
import com.example.chatapp.db.entity.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesPrivilegeJpaRepo extends JpaRepository<RolesPrivilege, RolesPrivilegeId> {
}
