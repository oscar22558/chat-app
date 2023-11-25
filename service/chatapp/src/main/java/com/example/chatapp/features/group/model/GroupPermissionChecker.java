package com.example.chatapp.features.group.model;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.entity.Member;

public class GroupPermissionChecker {
    public boolean hasPermission(AppUser operator, Member targetMember, Group group){
       var isAdmin = group.getMembers().stream()
               .filter(member -> member.getUser().getId().equals(operator.getId()))
               .findFirst()
               .orElseThrow(() -> )
    }
}
