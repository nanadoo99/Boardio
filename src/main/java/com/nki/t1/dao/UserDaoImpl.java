package com.nki.t1.dao;

import com.nki.t1.dto.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSession SqlSession;
    private static String namespace = "com.nki.t1.dao.UsersMapper.";

    @Override
    public UserDto selectUserById(UserDto userDto) {
        return SqlSession.selectOne(namespace + "selectUserById", userDto);
    }

    @Override
    public UserDto findById(String username) {
        return SqlSession.selectOne(namespace + "findById", username);
    }

    @Override
    public boolean existMember(UserDto userDto) {
        return SqlSession.selectOne(namespace + "existMember", userDto);
    }

    @Override
    public int insertUser(UserDto userDto) {
        return SqlSession.insert(namespace + "insertUser", userDto);
    }

    @Override
    public int idChk(String id) {
        return SqlSession.selectOne(namespace + "idChk", id);
    }

}
