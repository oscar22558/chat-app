package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "privileges")
public class Privilege{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeType name;
    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Privilege(PrivilegeType name) {
        this.name = name;
    }
}