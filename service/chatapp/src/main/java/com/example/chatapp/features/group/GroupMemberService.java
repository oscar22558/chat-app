package com.example.chatapp.features.group;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.features.user.UserIdentityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class GroupMemberService {
    GroupJpaRepo groupJpaRepo;
    AppUserJpaRepo appUserJpaRepo;
    UserIdentityService userIdentityService;

    public void addMember(long groupId, long userId){

    }

    public void addMembers(long groupId, List<Long> userIds){

    }

    public void removeMember(long groupId, long userId){

    }

    public void removeMembers(long groupId, List<Long> userIds){

    }

}
