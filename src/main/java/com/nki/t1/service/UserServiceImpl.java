package com.nki.t1.service;

import com.nki.t1.dao.UserDao;
import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserAdminDto;
import com.nki.t1.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int countUserByAdmin(SearchCondition sc) {
        return userDao.countUserByAdmin(sc);
    }

    @Override
    public List<UserAdminDto> selectUserListByAdmin(SearchCondition sc) {
        return userDao.selectUserListByAdmin(sc);
    }

    @Override
    public UserAdminDto selectUserByAdmin(int uno){
        return userDao.selectUserByAdmin(uno);
    }

    @Override
    public void updateUserRoleBySuperAdmin(UserDto userDto) {
        userDao.updateUserRoleBySuperAdmin(userDto);
    }
}
