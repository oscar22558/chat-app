package com.example.chatapp.converter;

import com.example.chatapp.common.mapper.DTOConverter;
import com.example.chatapp.db.entity.Member;
import com.example.chatapp.model.MemberDTO;

public class MemberDTOConverter implements DTOConverter<Member, MemberDTO> {
    @Override
    public MemberDTO map(Member input) {
        var memberDTO = new MemberDTO();
        var user = input.getUser();
        var username = user.getUsername();
        var groupRole = input.getGroupRoleType();

        memberDTO.setId(input.getId());
        memberDTO.setUserId(user.getId());
        memberDTO.setUsername(username);
        memberDTO.setGroupRole(groupRole);
        memberDTO.setMemberInvitationStatus(input.getInvitationStatus());
        return memberDTO;
    }

    @Override
    public Member reversedMap(MemberDTO output) {
        throw new UnsupportedOperationException();
    }
}
