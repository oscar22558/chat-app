package com.example.chatapp.features.friend;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Friend;
import com.example.chatapp.db.entity.FriendStatus;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.FriendJpaRepo;
import com.example.chatapp.features.contact.ContactService;
import com.example.chatapp.features.friend.model.*;
import com.example.chatapp.features.user.UserIdentityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendService {
    FriendJpaRepo friendJpaRepo;
    UserIdentityService userIdentityService;
    AppUserJpaRepo appUserJpaRepo;
    FriendStatusTransaction friendStatusTransaction;
    FriendViewMapper friendViewMapper;
    InvitationViewMapper invitationViewMapper;
    ContactService contactService;

    public List<FriendInvitationSearchUsersResult> searchUsersForFriendInvitation(String searchStr){
        Long authedUserId = userIdentityService.getUser().getId();
        var friends = friendViewMapper.map(userIdentityService.getUser().getFriends()).stream().map(FriendView::getUserId).toList();
        return appUserJpaRepo.findAppUsersByUsernameContainsIgnoreCase(searchStr)
                .stream()
                .filter(user -> !friends.contains((user.getId())) && !user.getId().equals(authedUserId))
                .map(user -> new FriendInvitationSearchUsersResult(user.getId(), user.getUsername()))
                .toList();
    }
    public List<FriendView> getFriends(String searchStr){
        var user = userIdentityService.getUser();
        friendViewMapper.setAuthedUserId(user.getId());
        return user.getFriends()
                .stream()
                .filter(friend -> friend.getStatus() == FriendStatus.ACCEPTED)
                .map(friendViewMapper::map)
                .filter(friendView -> friendView.getUsername().contains(searchStr))
                .toList();
    }

    public List<InvitationView> getFriendInvitations(InvitationType type){
        var user = userIdentityService.getUser();
        invitationViewMapper.setAuthedUserId(user.getId());
        return user.getFriends()
                .stream()
                .filter(friend -> friend.getStatus() == FriendStatus.PENDING)
                .map(invitationViewMapper::map)
                .filter(invitationView -> type == null || invitationView.getType() == type)
                .toList();
    }

    public List<FriendView> getFriendsExcludedUsernames(String searchStr, List<String> usernamesToExclude){
        var user = userIdentityService.getUser();
        friendViewMapper.setAuthedUserId(user.getId());
        return user.getFriends()
                .stream()
                .filter(friend -> friend.getStatus() == FriendStatus.ACCEPTED)
                .map(friendViewMapper::map)
                .filter(friend -> friend.getUsername().contains(searchStr)
                                && !usernamesToExclude.contains(friend.getUsername())
                        )
                .toList();
    }

    public FriendView sendFriendRequest(FriendRequest request){
        var target = appUserJpaRepo.findById(request.getUserId())
                .orElseThrow(RecordNotFoundException::new);
        var user = userIdentityService.getUser();

        var existingFriendRequest = user.getSentFriendRequests()
                .stream()
                .filter(friend -> friend.getRequestReceiver().getId().equals(request.getUserId()))
                .findFirst();
        if(existingFriendRequest.isPresent())
            return friendViewMapper.map(existingFriendRequest.get());

        var friend = new Friend();
        friend.setRequestSender(user);
        friend.setRequestReceiver(target);
        friend.setStatus(friendStatusTransaction.sendInvitation());
        friend.setCreateAt(AppTimestamp.newInstance());
        var savedFriend = friendJpaRepo.save(friend);
        return friendViewMapper.map(savedFriend);
    }

    public void acceptFriendRequest(Long userId){
        var authedUser = userIdentityService.getUser();
        var targetUser = appUserJpaRepo.findById(userId)
                .orElseThrow(RecordNotFoundException::new);

        var friendRequest = authedUser.getReceivedFriendRequests()
                .stream()
                .filter(request -> request.getRequestSender().getId().equals(userId))
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);

        friendRequest.setStatus(friendStatusTransaction.acceptRequest(friendRequest.getStatus()));
        friendRequest.setCreateAt(AppTimestamp.newInstance());
        friendJpaRepo.save(friendRequest);

        contactService.addContact(authedUser, targetUser);
    }

    public void revokeOrRejectFriendRequest(Long userId){
        var authedUser = userIdentityService.getUser();
        var targetUser = appUserJpaRepo.findById(userId).orElseThrow(RecordNotFoundException::new);
        var relationship = retrieveFriendRequest(authedUser, userId);
        friendStatusTransaction.revokeOrRejectRequest(relationship.getStatus());
        removeFriendRelationship(authedUser, targetUser, relationship);
    }

    public void removeFriend(Long userId){
        var authedUser = userIdentityService.getUser();
        var targetUser = appUserJpaRepo.findById(userId).orElseThrow(RecordNotFoundException::new);
        var relationship = retrieveFriendRequest(authedUser, userId);
        friendStatusTransaction.removeFriend(relationship.getStatus());
        removeFriendRelationship(authedUser, targetUser, relationship);
    }

    private void removeFriendRelationship(AppUser authedUser, AppUser targetUser, Friend relationship){
        authedUser.getReceivedFriendRequests().remove(relationship);
        authedUser.getSentFriendRequests().remove(relationship);
        targetUser.getReceivedFriendRequests().remove(relationship);
        targetUser.getSentFriendRequests().remove(relationship);
        friendJpaRepo.delete(relationship);
    }

    public void updateFriendStatus(Long userId, UpdateFriendStatusRequest request){
        if(request.getStatus() == FriendStatus.BLOCKED){
            blockFriend(userId);
        }else{
            unblockFriend(userId);
        }
    }

    public void blockFriend(Long userId){
        var authedUser = userIdentityService.getUser();
        var request = retrieveFriendRequest(authedUser, userId);
        var newStatus = friendStatusTransaction.block(request.getStatus());

        request.setStatus(newStatus);
        friendJpaRepo.save(request);
    }

    public void unblockFriend(Long userId){
        var authedUser = userIdentityService.getUser();
        var request = retrieveFriendRequest(authedUser, userId);
        var newStatus = friendStatusTransaction.unblock(request.getStatus());

        request.setStatus(newStatus);
        friendJpaRepo.save(request);
    }

    private Friend retrieveFriendRequest(AppUser authedUser, Long targetUserId){
        var receivedRequest = authedUser.getReceivedFriendRequests()
                .stream()
                .filter(request -> request.getRequestSender().getId().equals(targetUserId))
                .findFirst();
        var sentRequest = authedUser.getSentFriendRequests()
                .stream()
                .filter(request -> request.getRequestReceiver().getId().equals(targetUserId))
                .findFirst();

        if(receivedRequest.isEmpty() && sentRequest.isEmpty())
            throw new RecordNotFoundException();

        return receivedRequest.orElseGet(sentRequest::get);
    }
}
