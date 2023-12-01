package com.example.chatapp.features.group;

import com.example.chatapp.features.group.model.GroupCreateRequest;
import com.example.chatapp.features.group.model.GroupPutRequest;
import com.example.chatapp.features.user.UserIdentityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/group")
public class GroupController {

    GroupService service;
    UserIdentityService userIdentityService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable  Long id){
        var group = service.getGroup(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupCreateRequest request){
        var groupId = service.createGroup(request);
        //send an updated list of recipient to Websocket topic /topic/user/{id}/recipients
       return ResponseEntity.created(URI.create("/api/group/"+groupId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Long id, @RequestBody GroupPutRequest request){
        service.updateGroup(id, request);
        //send an updated list of recipient to Websocket topic /topic/user/{id}/recipients
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id){
        service.deleteGroup(id);
        //send an updated list of recipient to Websocket topic /topic/user/{id}/recipients
        return ResponseEntity.ok(null);
    }

}
