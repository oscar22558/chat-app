package com.example.chatapp.converter;

import com.example.chatapp.common.mapper.DTOConverter;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.repo.MemberJpaRepo;
import com.example.chatapp.model.GroupDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupDTOConverter implements DTOConverter<Group, GroupDTO> {
    MemberDTOConverter memberDTOConverter = new MemberDTOConverter();
    MemberJpaRepo memberJpaRepo;

    @Override
    public GroupDTO map(Group input) {
        var group = new GroupDTO();
        var id = input.getId();
        var name = input.getName();

        var members = memberJpaRepo.findByGroupId(input.getId());
        var memberDTOs = memberDTOConverter.map(members);

        group.setId(id);
        group.setName(name);
        group.setMemberDTOList(memberDTOs);
        return group;
    }

    @Override
    public Group reversedMap(GroupDTO output) {
        throw new UnsupportedOperationException();
    }
}
