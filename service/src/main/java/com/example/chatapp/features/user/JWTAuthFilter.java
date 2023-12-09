package com.example.chatapp.features.user;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.common.exception.UnAuthenticatedException;
import com.example.chatapp.features.user.model.SpringUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JWTAuthFilter extends OncePerRequestFilter {
    JWTService jwtService;
    UserIdentityService userIdentityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        authorizeUser(request);
        filterChain.doFilter(request, response);
    }

    private void authorizeUser(HttpServletRequest request){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) return;
        String accessToken = authHeader.replace("Bearer ", "");
        if(accessToken.isBlank()) throw new UnAuthenticatedException("Please sing in first.");

        Map<String, Object> claims = jwtService.parseToken(accessToken);
        Integer id = (Integer) claims.get("userId");
        try {
            SpringUser userDetails = userIdentityService.loadUserById(Integer.toUnsignedLong(id));
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RecordNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
