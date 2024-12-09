package com.example.chatapp.model;

import com.example.chatapp.db.entity.MemberInvitationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDTO {
    Long id;
    String name;
    List<MemberDTO> memberDTOList;
    MemberInvitationStatus invitationStatus;
}
