package com.example.chatapp.model;

import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.MemberInvitationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberDTO {
    Long id;
    Long userId;
    String username;
    GroupRoleType groupRole;
    MemberInvitationStatus memberInvitationStatus;
}
