package com.nki.t1.typeHandler;

import com.nki.t1.domain.ContentState;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContentStateTypeHandler extends BaseTypeHandler<ContentState> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ContentState contentState, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, contentState.name()); // contentState.name() >> 상수 contentState의 이름을 문자열로 반환
    }

    @Override
    public ContentState getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String code = resultSet.getString(s);
        return ContentState.valueOf(code); // 문자열을 해당 ContentState 상수로 반환
    }

    @Override
    public ContentState getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String code = resultSet.getString(i);
        return ContentState.valueOf(code);
    }

    @Override
    public ContentState getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String code = callableStatement.getString(i);
        return ContentState.valueOf(code);
    }
}
