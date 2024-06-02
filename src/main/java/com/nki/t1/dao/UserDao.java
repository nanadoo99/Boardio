package com.nki.t1.dao;

import com.nki.t1.dto.UserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDao {
    UserDto selectUserById(UserDto userDto);
    UserDto findById(String username) throws UsernameNotFoundException;
    boolean existMember(UserDto userDto);
    int insertUser(UserDto userDto);
    int idChk(String id); // 검토 : existMember 와 통합
}
