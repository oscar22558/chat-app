package com.example.chatapp.features.user;

import com.example.chatapp.common.exception.UnAuthenticatedException;
import com.example.chatapp.features.user.model.SpringUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthService {

    AuthenticationManager authenticationManager;
    UserIdentityService userIdentityService;

    public UserDetails confirmPassword(String password){
        if(userIdentityService.isAnonymous()) throw new UnAuthenticatedException("Please sign-in first.");
        SpringUser user = userIdentityService.getSpringUser();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), password);
        authentication = authenticationManager.authenticate(authentication);
        return (UserDetails) authentication.getPrincipal();
    }
}