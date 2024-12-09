package com.example.chatapp.features.message;

import com.example.chatapp.db.entity.MessageStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDTO {
    Long id;
    Long senderId;
    String senderUsername;
    String content;
    Timestamp sendAt;
    Timestamp readAt;
    MessageStatus status;
}
