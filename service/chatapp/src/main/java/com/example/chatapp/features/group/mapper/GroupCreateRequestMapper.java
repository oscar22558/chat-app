package com.example.chatapp.features.group.mapper;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.features.group.model.GroupCreateRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
public class GroupCreateRequestMapper implements ModelMapper<GroupCreateRequest, Group> {

    @Override
    public Group map(GroupCreateRequest input) {
        var name = input.getName();
        var group = new Group();
        group.setName(name);
        group.setMembers(new ArrayList<>());
        return group;
    }

    @Override
    public GroupCreateRequest reversedMap(Group output) {
        throw new UnsupportedOperationException();
    }
}
