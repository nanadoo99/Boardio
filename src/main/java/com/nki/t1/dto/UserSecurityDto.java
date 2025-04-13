package com.nki.t1.dto;

import com.nki.t1.domain.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserSecurityDto extends User implements ObjDto, OAuth2User {
    private Integer uno;
    private String id;
    private String email;
    //    private String password;
    @Enumerated(EnumType.STRING)
    private Role userRole; // 검토 : Role 이 여러개인 경우. 조인 테이블까지도..
    private boolean nonlocked;
    private boolean social;
    private Integer failures;
    private String accessToken;

    public UserSecurityDto(String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = username;
        this.email = email;
    }
    public UserSecurityDto(Integer uno, String username, String email, String password, Integer failures, Role userRole, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.uno = uno;
        this.id = username;
        this.email = email;
        this.failures = failures;
        this.userRole = userRole;
    }




/*    @Override
    public Collection<GrantedAuthority> getAuthorities() {
//        return this.getAuthorities();
        return authorities;
    }


    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }*/
    public UserDto toUserDto() {
        return UserDto.builder()
                .uno(this.uno)
                .id(this.id)
                .email(this.email)
                .social(this.social)
                .password(this.getPassword())
                .userRole(this.userRole)
                .failures(this.failures)
                .build();
    }


    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private Map<String, Object> props;

    @Override
    public Map<String, Object> getAttributes() {
        return props;
    }

    @Override
    public String getName() {
        return email;
    }



}
