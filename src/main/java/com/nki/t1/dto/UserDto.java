package com.nki.t1.dto;

import com.nki.t1.domain.Role;
import com.nki.t1.utils.DateTimeFormatUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto implements ObjDto {
    private Integer uno;
    private String id;
    private String email;
    private String password;
    private boolean social;
    private Collection<GrantedAuthority> authorities;
    @Enumerated(EnumType.STRING)
    private Role userRole;
    private boolean nonlocked;
    private Integer failures;
    private Date createdAt;
    private Date updatedAt;

    public UserDto() {}

    public UserSecurityDto toUserSecurityDto() {
        Collection<GrantedAuthority> grantedAuthorities = this.authorities;

        // authorities가 null인 경우 기본 권한 설정
        if (grantedAuthorities == null) {
            grantedAuthorities = new ArrayList<>();
            if (this.userRole != null) {
                grantedAuthorities.add(new SimpleGrantedAuthority(this.userRole.getValue()));
            } else {
                grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
            }
        }
        return new UserSecurityDto(
                this.uno,
                this.id,
                this.email,
                this.password,
                this.failures,
                this.getUserRole(),
                grantedAuthorities
        );
    }

    public boolean isNonlocked() {
        return this.nonlocked;
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public UserDto(UserDtoBuilder builder){
        this.uno = builder.uno;
        this.id = builder.id;
        this.email = builder.email;
        this.userRole = builder.userRole;
        this.password = builder.password;
        this.failures = builder.failures;
        this.social = builder.social;
    }

    public static class UserDtoBuilder{
        private Integer uno;
        private String id;
        private String email;
        private String password;
        private Role userRole;
        private Integer failures;
        private boolean social;
        private Collection<GrantedAuthority> authorities;


        public UserDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder uno(Integer uno) {
            this.uno = uno;
            return this;
        }
        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder userRole(Role userRole) {
            this.userRole = userRole;
            return this;
        }

        public UserDtoBuilder failures(Integer failures) {
            this.failures = failures;
            return this;
        }

        public UserDtoBuilder social(boolean social) {
            this.social = social;
            return this;
        }

        public UserDtoBuilder authorities(Collection<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }


        public UserDto build() {
            return new UserDto(this);
        }
    }

    // Date 포맷팅 - 날짜
    protected String getFormattedDate(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedCreatedAt() {
        return getFormattedDate(this.createdAt);
    }

    // Date 포맷팅 - 날짜 및 시간
     protected String getFormattedDateTime(Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateTimeToFormattedString(date);
    }

    public String getFormattedCreatedAtTime() {
        return getFormattedDateTime(this.createdAt);
    }

}
