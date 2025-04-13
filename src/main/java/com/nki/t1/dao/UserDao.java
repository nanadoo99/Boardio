package com.nki.t1.dao;

import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserAdminDto;
import com.nki.t1.dto.UserDto;

import java.util.List;

public interface UserDao {
    UserDto findByEmail(String username);
    boolean existMemberId(UserDto userDto);
    boolean existMemberEmail(UserDto userDto);
    int insertUser(UserDto userDto);
    int idChk(String id); // 검토 : existMember 와 통합
    int emailChk(String email); // 검토 : existMember 와 통합
    UserDto selectUserByUno(int uno);
    int updateUser(UserDto userDto);
    void increaseFailures(UserDto userDto);
    void resetFailures(int uno);
    List<String> existingSimilarIdList (String id);
    int updatePwByEmail(UserDto userDto);
    int deleteUserByUno(UserDto userDto);
    int deleteAdminByUno(UserDto userDto);

    int countUserByAdmin(SearchCondition sc);
    List<UserAdminDto> selectUserListByAdmin(SearchCondition sc);
    UserAdminDto selectUserByAdmin(int uno);
    void updateUserRoleBySuperAdmin(UserDto userDto);

}
