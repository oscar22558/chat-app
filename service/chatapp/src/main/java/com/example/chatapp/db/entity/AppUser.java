package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column
    Timestamp createAt;

    @Column
    String username;

    @Column
    String password;

    @OneToMany(mappedBy = "user")
    List<Member> groupMembers;

    @OneToMany(mappedBy = "sender")
    List<Message> sentMessages;

    @ManyToMany(mappedBy = "members")
    Set<Group> groups;

    @ManyToMany(mappedBy = "users")
    Set<Role> roles;
}
