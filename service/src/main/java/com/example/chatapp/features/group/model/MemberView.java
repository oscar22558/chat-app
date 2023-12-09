package com.example.chatapp.features.group.model;

import com.example.chatapp.db.entity.GroupRoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberView {
    Long userId;
    String username;
    GroupRoleType groupRole;
}
