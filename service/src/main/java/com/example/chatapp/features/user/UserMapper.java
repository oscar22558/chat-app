package com.example.chatapp.features.user;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.features.user.model.UserView;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements ModelMapper<AppUser, UserView> {
    @Override
    public UserView map(AppUser input) {
        return new UserView(input.getId(), input.getUsername());
    }

    @Override
    public AppUser reversedMap(UserView output) {
        var id = output.getId();
        var username = output.getUsername();
        var appUser = new AppUser();
        appUser.setId(id);
        appUser.setUsername(username);
        return appUser;
    }
}
