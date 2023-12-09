package com.example.chatapp.features.user.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {
    public static final class ErrorCode{
        public static final String USERNAME_EMPTY = "USERNAME_EMPTY";
        public static final String PASSWORD_EMPTY = "PASSWORD_EMPTY";
    }

    @NotEmpty(message = AuthRequest.ErrorCode.USERNAME_EMPTY)
    private String username;
    @NotEmpty(message = AuthRequest.ErrorCode.PASSWORD_EMPTY)
    private String password;
}