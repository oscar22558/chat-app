package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "contacts")
@NoArgsConstructor
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column(nullable = false)
    Timestamp updatedAt;

    @Column(nullable = false)
    Long recipientId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RecipientType recipientType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    AppUser user;
}
