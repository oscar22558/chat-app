package com.example.chatapp.redis.entity;

import com.example.chatapp.db.entity.RecipientType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Timestamp;

@RedisHash("contact")
@TypeAlias("contact")
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contact implements Serializable {
    @Id
    String id;
    Timestamp updatedAt;
    Integer newMsgCount = 0;
    Integer msgCount = 0;
    Integer readMsgCount = 0;
    @Indexed
    Long recipientId;
    @Indexed
    RecipientType recipientType;
    @Indexed
    Long userId;
}
