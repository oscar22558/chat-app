package com.example.chatapp.features.message;

import com.example.chatapp.db.entity.RecipientType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {
    String content;
    Timestamp sendAt;
    Long recipientId;
    RecipientType recipientType;
}
