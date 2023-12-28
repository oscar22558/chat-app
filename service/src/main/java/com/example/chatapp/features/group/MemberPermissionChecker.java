package com.example.chatapp.features.group;

import com.example.chatapp.common.exception.UnAuthorizedException;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@NoArgsConstructor
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberPermissionChecker {
    Group group;

    public Optional<Member> isMember(long userId){
        return group.getMembers().stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst();
    }

    public boolean isNotGroupAdmin(Member member){
        return member.getGroupRoleType() != GroupRoleType.ADMIN;
    }

    public void throwIfIsNotMember(long userId){
        isMember(userId).orElseThrow(UnAuthorizedException::new);
    }

    public void canGetGroup(long userId){
        isMember(userId).orElseThrow(UnAuthorizedException::new);
    }

    public void canRemoveMember(long operatorUserId, long userId){
        var memberOperator = isMember(operatorUserId).orElseThrow(UnAuthorizedException::new);
        if(isNotGroupAdmin(memberOperator))
            throw new UnAuthorizedException();

        throwIfIsNotMember(userId);
    }

    public void canRemoveMembers(long operatorUserId, List<Long> userIds){
        var memberOperator = isMember(operatorUserId).orElseThrow(UnAuthorizedException::new);
        if(isNotGroupAdmin(memberOperator))
            throw new UnAuthorizedException();

        userIds.forEach(this::throwIfIsNotMember);
    }

    public void canAddMember(long operatorUserId){
        var memberOperator = isMember(operatorUserId).orElseThrow(UnAuthorizedException::new);
        if(isNotGroupAdmin(memberOperator))
            throw new UnAuthorizedException();
    }
}
