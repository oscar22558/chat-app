package com.example.chatapp.features.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/user")
public class SearchUserController {
    SearchUserService service;
    @GetMapping("/invitation-status/search")
    public ResponseEntity<?> searchUser(@RequestParam Long groupId, @RequestParam String username){
        var searchResult = service.search(groupId, username);
        return ResponseEntity.ok(searchResult);
    }
}
