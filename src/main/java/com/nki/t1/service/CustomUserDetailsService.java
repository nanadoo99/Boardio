package com.nki.t1.service;

import com.nki.t1.dto.UserDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

// 공부: https://stackoverflow.com/questions/65235534/spring-error-bean-named-x-is-expected-to-be-of-type-y-but-was-actually-of
public interface CustomUserDetailsService extends UserDetailsService {
    UserDto getUserDtoByEmail(String email);
    int register(UserDto userDto, HttpServletRequest request) throws RuntimeException;
    int idChk(String id);
    int emailChk(String email);
    UserDto getUserDto(int uno);
    void updateUser(UserDto userDto, HttpServletRequest request) throws RuntimeException;
    void increaseFailures(UserDto userDto);
    void resetFailures(int uno);
    void checkFailure(String attemptPwd, UserDto userDto) throws LockedException, BadCredentialsException;
    void deactivate(HttpServletRequest request);
}