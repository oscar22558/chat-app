package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "friends")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    Long id;

    @Column(nullable = false)
    Timestamp createAt;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    AppUser requestSender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    AppUser requestReceiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    FriendStatus status;
}
