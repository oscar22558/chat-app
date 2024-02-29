package com.example.chatapp.features.group;

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
@RequestMapping("/api")
public class SearchUserController {
    SearchUserService service;
    @GetMapping("/group/invitation/user-search")
    public ResponseEntity<?> searchAllUser(
            @RequestParam Long groupId,
            @RequestParam(name = "search", defaultValue = "") String searchStr){
        var searchResult = service.searchAllUsers(groupId, searchStr);
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/group/invitation/friend-search")
    public ResponseEntity<?> searchFriend(
            @RequestParam Long groupId,
            @RequestParam(name = "search", defaultValue = "") String searchStr){
        var searchResult = service.searchFriends(groupId, searchStr);
        return ResponseEntity.ok(searchResult);
    }
}
