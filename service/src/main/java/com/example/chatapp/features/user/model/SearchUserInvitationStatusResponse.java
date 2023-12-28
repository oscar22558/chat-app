package com.example.chatapp.features.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchUserInvitationStatusResponse {
    Long id;
    String username;
    String invitationStatus;
}
