package com.nki.t1.security;

import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.domain.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";
    private final UserSecurityDto userDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = userDto.getUserRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDto.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}