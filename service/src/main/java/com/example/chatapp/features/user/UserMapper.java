package com.example.chatapp.features.user;

import com.example.chatapp.common.mapper.DTOConverter;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.features.user.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DTOConverter<AppUser, UserDTO> {
    @Override
    public UserDTO map(AppUser input) {
        return new UserDTO(input.getId(), input.getUsername());
    }

    @Override
    public AppUser reversedMap(UserDTO output) {
        var id = output.getId();
        var username = output.getUsername();
        var appUser = new AppUser();
        appUser.setId(id);
        appUser.setUsername(username);
        return appUser;
    }
}
