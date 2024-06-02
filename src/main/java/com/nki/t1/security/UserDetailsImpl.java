package com.nki.t1.security;

import com.nki.t1.dto.UserDto;
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
    private final UserDto member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("@@@@@ Start of UserDetailsImpl_getAuthorities");
        Role role = member.getUserRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>(); //List인 이유 : 여러개의 권한을 가질 수 있다
        authorities.add(authority);
        for(GrantedAuthority auth:authorities){
            System.out.println("----- auth = " + auth.toString());
        }
        System.out.println("@@@@@ End of UserDetailsImpl_getAuthorities");

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getId();
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
}