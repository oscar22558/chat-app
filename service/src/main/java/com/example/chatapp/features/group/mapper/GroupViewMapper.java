package com.example.chatapp.features.group.mapper;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.features.group.model.GroupView;

public class GroupViewMapper implements ModelMapper<Group, GroupView> {
    MemberViewMapper memberViewMapper = new MemberViewMapper();

    @Override
    public GroupView map(Group input) {
        var id = input.getId();
        var name = input.getName();
        var members = memberViewMapper.map(input.getMembers());
        return new GroupView(id, name, members);
    }

    @Override
    public Group reversedMap(GroupView output) {
        throw new UnsupportedOperationException();
    }
}
