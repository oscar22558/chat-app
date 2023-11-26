package com.example.chatapp.features.group.model;

import com.example.chatapp.common.exception.UnAuthorizedException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.entity.GroupRoleType;
import com.example.chatapp.db.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupPermissionChecker {
    public void hasPermission(long operatorUserId, long targetUserId, Group group){
       var memberOperator = group.getMembers().stream()
               .filter(member -> member.getUser().getId().equals(operatorUserId))
               .findFirst()
               .orElseThrow(UnAuthorizedException::new);

       if(memberOperator.getGroupRoleType() != GroupRoleType.ADMIN)
           throw new UnAuthorizedException();

       group.getMembers().stream()
               .filter(member -> member.getUser().getId().equals(targetUserId))
               .findFirst()
               .orElseThrow(UnAuthorizedException::new);
    }

    public void hasPermission(long operatorUserId, List<Long> targetUserIds, Group group){
        targetUserIds.forEach(targetUserId -> hasPermission(operatorUserId, targetUserId, group));
    }
}
