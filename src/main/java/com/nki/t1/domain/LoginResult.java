package com.nki.t1.domain;

import com.nki.t1.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResult { // 검토 : 실제 사용 안함. 삭제 고려.
    private boolean success; // 로그인 성공 여부
    private String message; // 로그인 실패 시 에러 메시지
    private UserDto userDto;

    public LoginResult() {}

    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResult(boolean success, UserDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }
}
