package com.nki.t1.dto;

import com.nki.t1.domain.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto {
    private Integer uno;
    private String id;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role userRole; // 검토 : Role 이 여러개인 경우. 조인 테이블까지도..
    private Date birthday;
    private Date createdAt;
    private Date updatedAt;

    public UserDto() {}

    public UserDto(UserDto userDto) {
        this.id = userDto.getId();
        this.password = userDto.getPassword();
        this.userRole = Role.USER;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

}
