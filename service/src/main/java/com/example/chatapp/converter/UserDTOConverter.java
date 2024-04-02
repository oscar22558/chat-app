package com.example.chatapp.converter;

import com.example.chatapp.common.mapper.DTOConverter;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.features.user.model.UserDTO;

public class UserDTOConverter implements DTOConverter<AppUser, UserDTO> {
    @Override
    public UserDTO map(AppUser input) {
        var userDto = new UserDTO();
        userDto.setId(input.getId());
        userDto.setUsername(input.getUsername());
        return userDto;
    }

    @Override
    public AppUser reversedMap(UserDTO output) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
