package com.nki.t1.security;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.UserDto;
import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.exception.BadRequestException;
import com.nki.t1.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${auth.max.attempts}")
    private Integer maxAttempts;

    private CustomUserDetailsService userDetailsServiceImpl;
    private PasswordEncoder passwordEncoder;


    public void setUserDetailsService(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsServiceImpl = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // UserDetailsImpl 에서 getUsername() > return member.getId();
        String password = (String) authentication.getCredentials();

        if (password == null || password.isEmpty() || username == null || username.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("empty id or password");
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        UserSecurityDto userSecurityDto;
        UserDto userDto;
        if (userDetails instanceof UserSecurityDto) {
            userSecurityDto = (UserSecurityDto) userDetails;
            userDto = userSecurityDto.toUserDto();
            userDetailsServiceImpl.checkFailure(password, userDto);
        } else {
            throw new BadRequestException(ErrorType.REQUEST_FAILED);
        }

        userDetailsServiceImpl.resetFailures(userDto.getUno());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userSecurityDto, null, userSecurityDto.getAuthorities());

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}