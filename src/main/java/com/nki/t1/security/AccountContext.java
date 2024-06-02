package com.nki.t1.security;

import com.nki.t1.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountContext extends User {

    private final UserDto account;

    public AccountContext(UserDto account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getId(), account.getPassword(), authorities);
        this.account = account;
    }

    public UserDto getAccount() {
        return account;
    }
}