package com.example.chatapp.features.friend;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Friend;
import com.example.chatapp.db.entity.FriendStatus;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.FriendJpaRepo;
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
    FriendStatusChecker friendStatusChecker;
    FriendViewMapper friendViewMapper;
    InvitationViewMapper invitationViewMapper;

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
        friend.setStatus(FriendStatus.PENDING);
        friend.setCreateAt(AppTimestamp.newInstance());
        var savedFriend = friendJpaRepo.save(friend);
        return friendViewMapper.map(savedFriend);
    }

    public void acceptFriendRequest(Long userId){
        var authedUser = userIdentityService.getUser();

        var friendRequest = authedUser.getReceivedFriendRequests()
                .stream()
                .filter(request -> request.getRequestSender().getId().equals(userId))
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);

        if(!friendStatusChecker.isValid(friendRequest.getStatus(), FriendStatus.ACCEPTED))
            throw new FriendStatusTransactionException();
        friendRequest.setStatus(FriendStatus.ACCEPTED);
        friendRequest.setCreateAt(AppTimestamp.newInstance());
        friendJpaRepo.save(friendRequest);
    }

    public void revokeOrRejectFriendRequest(Long userId){
        removeFriend(userId);
    }
    public void removeFriend(Long userId){
        var authedUser = userIdentityService.getUser();
        var targetUser = appUserJpaRepo.findById(userId).orElseThrow(RecordNotFoundException::new);
        var request = retrieveFriendRequest(authedUser, userId);

        authedUser.getReceivedFriendRequests().remove(request);
        authedUser.getSentFriendRequests().remove(request);
        targetUser.getReceivedFriendRequests().remove(request);
        targetUser.getSentFriendRequests().remove(request);
        friendJpaRepo.delete(request);
    }

    public void updateFriendStatus(Long userId, UpdateFriendStatusRequest request){
        if(request.getStatus() == FriendStatus.BLOCKED){
            blockFriend(userId);
        }else {
            unblockFriend(userId);
        }
    }

    public void blockFriend(Long userId){
        var authedUser = userIdentityService.getUser();
        var request = retrieveFriendRequest(authedUser, userId);

        if(!friendStatusChecker.isValid(request.getStatus(), FriendStatus.BLOCKED))
            throw new FriendStatusTransactionException();
        request.setStatus(FriendStatus.BLOCKED);
        friendJpaRepo.save(request);
    }

    public void unblockFriend(Long userId){
        var authedUser = userIdentityService.getUser();
        var request = retrieveFriendRequest(authedUser, userId);

        if(!friendStatusChecker.isValid(request.getStatus(), FriendStatus.ACCEPTED))
            throw new FriendStatusTransactionException();
        request.setStatus(FriendStatus.ACCEPTED);
        friendJpaRepo.save(request);
    }

    public Friend retrieveFriendRequest(AppUser authedUser, Long targetUserId){
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