package com.example.chatapp.features.user;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Privilege;
import com.example.chatapp.db.entity.Role;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GrantedAuthoritiesRetriever {
    GrantedAuthoritiesMapper grantedAuthoritiesMapper = new GrantedAuthoritiesMapper();
    public List<GrantedAuthority> getAuthorities(AppUser user){
        Stream<Role> rolesStream = user.getRoles()
                .stream();
        Stream<Privilege> privilegesStream = rolesStream.flatMap(role -> role.getPrivileges().stream());
        Stream<String> privilegeName = privilegesStream.map(privilege -> privilege.getName().name());
        List<String> privileges = privilegeName.toList();
        List<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList());
        roles.addAll(privileges);
        return grantedAuthoritiesMapper.map(roles);
    }
}
