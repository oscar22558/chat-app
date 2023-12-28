package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    GroupRoleType groupRoleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MemberInvitationStatus invitationStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    AppUser user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    Group group;
}
