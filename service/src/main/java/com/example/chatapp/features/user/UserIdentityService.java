package com.example.chatapp.features.user;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.features.user.model.SpringUser;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Transactional
public class UserIdentityService implements UserDetailsService {

    AppUserJpaRepo appUserJapRepo;
    GrantedAuthoritiesRetriever grantedAuthoritiesRetriever = new GrantedAuthoritiesRetriever();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserJapRepo.findAppUserByUsername(username);
        if(appUser == null) throw new UsernameNotFoundException("No such user " + username);
        var grantedAuthorities = grantedAuthoritiesRetriever.getAuthorities(appUser);
        return new SpringUser(appUser, grantedAuthorities);
    }

    public SpringUser loadUserById(Long id) throws RecordNotFoundException {
        AppUser appUser = appUserJapRepo.findById(id).orElseThrow(()->new RecordNotFoundException("User of id "+id+" not found."));
        var grantedAuthorities = grantedAuthoritiesRetriever.getAuthorities(appUser);
        return new SpringUser(appUser, grantedAuthorities);
    }


    public boolean isAnonymous() {
        return getSpringUser() == null;
    }
    public SpringUser getSpringUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return principal.equals("anonymousUser")
                ? null
                : (SpringUser) principal;
    }

    public AppUser getUser(){
        String username = getSpringUser().getUsername();
        return appUserJapRepo.findAppUserByUsername(username);
    }

    public Long getUserId(){
        String username = getSpringUser().getUsername();
        return appUserJapRepo.findAppUserByUsername(username).getId();
    }
}
