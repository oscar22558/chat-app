package com.example.chatapp.features.friend;

import com.example.chatapp.features.friend.model.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class FriendController {
    FriendService friendService;

    @GetMapping("/invitation/user-search")
    public ResponseEntity<?> searchUsersForFriendInvitation(@RequestParam("search") String searchStr){
        var users = friendService.searchUsersForFriendInvitation(searchStr);
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<List<FriendView>> getFriends(
            @RequestParam(value = "search", defaultValue = "") String searchStr
    ){
        var friendViews = friendService.getFriends(searchStr);
        return ResponseEntity.ok(friendViews);
    }

    @GetMapping("/invitation")
    public ResponseEntity<List<InvitationView>> getFriendInvitations(
            @RequestParam InvitationType type
            ){
        var invitations = friendService.getFriendInvitations(type);
        return ResponseEntity.ok(invitations);
    }

    @PostMapping
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest request){
        var friendView = friendService.sendFriendRequest(request);
        var uri = MvcUriComponentsBuilder
                .fromMethodName(FriendController.class, "getFriends", friendView.getUsername())
                .buildAndExpand(1)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/invitation/{userId}")
    public ResponseEntity<?> revokeOrRejectFriendRequest(@PathVariable Long userId){
        friendService.revokeOrRejectFriendRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/invitation/{userId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long userId){
        friendService.acceptFriendRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long userId){
        friendService.removeFriend(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateFriendStatus(
            @PathVariable Long userId,
            @RequestBody UpdateFriendStatusRequest request
    ){
        friendService.updateFriendStatus(userId, request);
        return ResponseEntity.noContent().build();
    }
}
