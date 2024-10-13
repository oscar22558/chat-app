package com.example.chatapp.redis.entity;

import com.example.chatapp.db.entity.RecipientType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.sql.Timestamp;

@RedisHash("contact")
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contact implements Serializable {
    @Id
    String id;
    Timestamp updatedAt;
    int newMsgCount;
    Long recipientId;
    RecipientType recipientType;
    Long userId;
}
