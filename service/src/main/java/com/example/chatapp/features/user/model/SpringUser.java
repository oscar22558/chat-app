package com.example.chatapp.features.user.model;

import com.example.chatapp.db.entity.AppUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpringUser implements UserDetails {
    AppUser appUser;
    List<GrantedAuthority> grantedAuthorities;
    public SpringUser(AppUser appUser, List<GrantedAuthority> grantedAuthorities) {
        this.appUser = appUser;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId(){
        return appUser.getId();
    }
    public AppUser getUser(){ return appUser; }

}
