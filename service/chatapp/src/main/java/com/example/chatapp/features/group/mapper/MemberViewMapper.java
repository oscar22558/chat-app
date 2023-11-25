package com.example.chatapp.features.group.mapper;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.features.group.model.MemberView;

public class MemberViewMapper implements ModelMapper<Member, MemberView> {
    @Override
    public MemberView map(Member input) {
        var id = input.getId();
        var username = input.getUser().getUsername();
        var groupRole = input.getGroupRoleType();
        return new MemberView(id, username, groupRole);
    }

    @Override
    public Member reversedMap(MemberView output) {
        throw new UnsupportedOperationException();
    }
}
