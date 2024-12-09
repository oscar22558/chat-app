package com.example.chatapp.features.user;

import com.example.chatapp.common.mapper.DTOConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class GrantedAuthoritiesMapper implements DTOConverter<String, GrantedAuthority> {
    @Override
    public GrantedAuthority map(String input) {
        return new SimpleGrantedAuthority(input);
    }

    @Override
    public String reversedMap(GrantedAuthority output) {
        return output.getAuthority();
    }
}
