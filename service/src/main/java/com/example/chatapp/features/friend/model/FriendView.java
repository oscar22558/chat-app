package com.example.chatapp.features.friend.model;

import com.example.chatapp.db.entity.FriendStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class FriendView {
    Long userId;
    Timestamp createAt;
    String username;
}
