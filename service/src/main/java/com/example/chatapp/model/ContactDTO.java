package com.example.chatapp.model;

import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.features.user.model.UserDTO;
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
public class ContactDTO {
    Long userId;
    Long recipientId;
    RecipientType recipientType;
    String recipientName;
    Timestamp updatedAt;
    Integer newMsgCount;
    Integer msgCount;
    Integer readMsgCount;
    UserDTO recipientUser;
    GroupDTO recipientGroup;
}
