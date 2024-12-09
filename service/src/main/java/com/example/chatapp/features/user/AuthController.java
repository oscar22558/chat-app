package com.example.chatapp.features.user;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.features.user.model.AuthRequest;
import com.example.chatapp.features.user.model.UserDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AuthController {

    JWTService jwtService;
    AuthenticationManager authenticationManager;
    UserIdentityService userIdentityService;
    UserMapper userMapper = new UserMapper();

    @PostMapping("/api/auth")
    public ResponseEntity<Map<String, String>> issueToken(@Valid @RequestBody AuthRequest request) {
        String token = jwtService.generateToken(request, authenticationManager);
        Map<String, String> response = Collections.singletonMap("token", token);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/auth/user")
    public ResponseEntity<UserDTO> user(){
        AppUser user = userIdentityService.getUser();
        var view = userMapper.map(user);
        return ResponseEntity.ok(view);
    }
}