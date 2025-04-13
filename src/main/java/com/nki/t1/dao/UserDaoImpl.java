package com.nki.t1.dao;

import com.nki.t1.dto.SearchCondition;
import com.nki.t1.dto.UserAdminDto;
import com.nki.t1.dto.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.UsersMapper.";

    @Override
    public UserDto findByEmail(String username) {
        return sqlSession.selectOne(namespace + "findByEmail", username);
    }

    @Override
    public boolean existMemberId(UserDto userDto) {
        return sqlSession.selectOne(namespace + "existMemberId", userDto);
    }

    @Override
    public boolean existMemberEmail(UserDto userDto) {
        return sqlSession.selectOne(namespace + "existMemberEmail", userDto);
    }

    @Override
    public int insertUser(UserDto userDto) {
        return sqlSession.insert(namespace + "insertUser", userDto);
    }

    @Override
    public int idChk(String id) {
        return sqlSession.selectOne(namespace + "idChk", id);
    }

    @Override
    public int emailChk(String id) {
        return sqlSession.selectOne(namespace + "emailChk", id);
    }

    @Override
    public UserDto selectUserByUno(int uno) {
        return sqlSession.selectOne(namespace + "selectUserByUno", uno);
    }

    @Override
    public int updateUser(UserDto userDto) {
        return sqlSession.update(namespace + "updateUser", userDto);
    }

    @Override
    public void increaseFailures(UserDto userDto) {
        sqlSession.update(namespace + "increaseFailures", userDto);
    }

    @Override
    public void resetFailures(int uno) {
        sqlSession.update(namespace + "resetFailures", uno);
    }

    @Override
    public List<String> existingSimilarIdList(String id) {
        return sqlSession.selectList(namespace + "existingSimilarIdList", id);
    }

    @Override
    public int updatePwByEmail(UserDto userDto) {
        return sqlSession.update(namespace + "updatePwByEmail", userDto);
    }

    @Override
    public int deleteUserByUno(UserDto userDto) {
        return sqlSession.delete(namespace + "deleteUserByUno", userDto);
    }

    @Override
    public int deleteAdminByUno(UserDto userDto) {
        return sqlSession.delete(namespace + "deleteAdminByUno", userDto);
    }
    
    @Override
    public int countUserByAdmin(SearchCondition sc) {
        return sqlSession.selectOne(namespace + "countUserByAdmin", sc);
    }
    
    @Override
    public List<UserAdminDto> selectUserListByAdmin(SearchCondition sc) {
        return sqlSession.selectList(namespace + "selectUserListByAdmin", sc);
    }

    @Override
    public UserAdminDto selectUserByAdmin(int uno) {
        return sqlSession.selectOne(namespace + "selectUserByAdmin", uno);
    }

    @Override
    public void updateUserRoleBySuperAdmin(UserDto userDto) {
        sqlSession.update(namespace + "updateUserRoleBySuperAdmin", userDto);
    }

}

