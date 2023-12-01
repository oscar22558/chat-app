package com.example.chatapp.features.contact.model;

import com.example.chatapp.db.entity.RecipientType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactView {
    Long userId;
    Long recipientId;
    RecipientType recipientType;
    Timestamp updatedAt;
}
