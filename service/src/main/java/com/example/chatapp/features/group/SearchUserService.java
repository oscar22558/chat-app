package com.example.chatapp.features.group;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.features.friend.FriendService;
import com.example.chatapp.features.user.UserIdentityService;
import com.example.chatapp.features.group.model.SearchUserInvitationStatusResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SearchUserService {
    AppUserJpaRepo appUserJpaRepo;
    GroupJpaRepo groupJpaRepo;
    UserIdentityService userIdentityService;
    FriendService friendService;

    public List<SearchUserInvitationStatusResponse> searchAllUsers(long groupId, String username){
        var authedUserId = userIdentityService.getUserId();
        var members = getMembers(groupId);

        return appUserJpaRepo
                .findAppUsersByUsernameContainsIgnoreCase(username)
                .stream()
                .filter(user -> !user.getId().equals(authedUserId))
                .map(user -> {
                    var groupMembership = members.stream()
                            .filter(member -> member.getUser().getId().equals(user.getId()))
                            .findFirst()
                            .flatMap(member -> Optional.of(member.getInvitationStatus().toString()));
                    var status = groupMembership.orElse("");
                    return new SearchUserInvitationStatusResponse(
                            user.getId(),
                            user.getUsername(),
                            status
                    );
                })
                .toList();
    }

    public List<SearchUserInvitationStatusResponse> searchFriends(long groupId, String searchStr){
        var members = getMembers(groupId);
        var memberUsernames = members.stream().map(member -> member.getUser().getUsername()).toList();
        return friendService
                .getFriendsExcludedUsernames(searchStr, memberUsernames)
                .stream()
                .map(friendView -> new SearchUserInvitationStatusResponse(
                        friendView.getUserId(),
                        friendView.getUsername(),
                        ""
                )).toList();
    }

    private List<Member> getMembers(long groupId){
        return groupJpaRepo.findById(groupId)
                .orElseThrow(RecordNotFoundException::new)
                .getMembers()
                .stream()
                .toList();
    }
}
