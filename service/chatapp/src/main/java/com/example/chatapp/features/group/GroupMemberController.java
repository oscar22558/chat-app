package com.example.chatapp.features.group;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/group/{id}")
public class GroupMemberController {

    GroupMemberService service;

    @PostMapping("/member/{userId}")
    public ResponseEntity<?> addMember(@PathVariable Long id, @PathVariable Long userId){
        service.addMember(id, userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/member/{userId}")
    public ResponseEntity<?> removeMember(@PathVariable Long id, @PathVariable Long userId){
        service.removeMember(id, userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/member")
    public ResponseEntity<?> leaveGroup(@PathVariable Long id){
        service.leaveGroup(id);
        return ResponseEntity.ok(null);
    }
}
