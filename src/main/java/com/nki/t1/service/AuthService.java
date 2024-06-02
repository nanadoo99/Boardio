package com.nki.t1.service;

import com.nki.t1.domain.LoginResult;
import com.nki.t1.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    public LoginResult loginPwChk(UserDto userDto);
    public int register(UserDto userDto, HttpServletRequest request);
    public int idChk(String id);
}
