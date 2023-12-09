package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "roles")
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    private Set<AppUser> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"
            )
    )
    private Set<Privilege> privileges;

    public Role(RoleType name){
        this.name = name;
    }

    public void addPrivilege(Privilege privilege){
        privileges.add(privilege);
        privilege.getRoles().add(this);
    }
    public void removePrivilege(Privilege privilege){
        privileges.remove(privilege);
        privilege.getRoles().remove(this);
    }
}