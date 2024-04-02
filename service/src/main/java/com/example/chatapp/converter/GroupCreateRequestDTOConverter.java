package com.example.chatapp.converter;

import com.example.chatapp.common.mapper.DTOConverter;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.features.group.model.GroupCreateRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;


@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
public class GroupCreateRequestDTOConverter implements DTOConverter<GroupCreateRequest, Group> {

    @Override
    public Group map(GroupCreateRequest input) {
        var name = input.getName();
        var group = new Group();
        group.setName(name);
        group.setMembers(new ArrayList<>());
        group.setIsDeleted(false);
        return group;
    }

    @Override
    public GroupCreateRequest reversedMap(Group output) {
        throw new UnsupportedOperationException();
    }
}
