package com.nki.t1.service;

import com.nki.t1.dto.*;

import java.util.List;

public interface UserService {
    int countUserByAdmin(SearchCondition sc);
    List<UserAdminDto> selectUserListByAdmin(SearchCondition sc);
    UserAdminDto selectUserByAdmin(int uno);
    void updateUserRoleBySuperAdmin(UserDto userDto);
}
