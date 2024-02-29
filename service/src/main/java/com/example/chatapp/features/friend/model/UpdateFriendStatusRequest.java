package com.example.chatapp.features.friend.model;

import com.example.chatapp.db.entity.FriendStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFriendStatusRequest {
    FriendStatus status;
}
