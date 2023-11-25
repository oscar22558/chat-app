package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column
    String mediaUrl;

    @Column(nullable = false)
    String content;

    @Column
    Timestamp sendAt;

    @Column
    Timestamp readAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    MessageStatus status;

    @Column(nullable = false)
    Long recipientId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RecipientType recipientType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    AppUser sender;

}
