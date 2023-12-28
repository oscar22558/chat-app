package com.example.chatapp.features.user;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.features.user.model.SearchUserInvitationStatusResponse;
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

    public List<SearchUserInvitationStatusResponse> search(long groupId, String username){
        var authedUserId = userIdentityService.getUserId();
        var members = groupJpaRepo.findById(groupId)
                .orElseThrow(RecordNotFoundException::new)
                .getMembers()
                .stream()
                .toList();

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
}
