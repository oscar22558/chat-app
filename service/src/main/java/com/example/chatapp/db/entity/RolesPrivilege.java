package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles_privileges")
public class RolesPrivilege {
    @EmbeddedId
    private RolesPrivilegeId id;

    @MapsId("privilegeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "privilege_id", nullable = false)
    private Privilege privilege;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}