package com.nki.t1.security;

import com.nki.t1.dao.UserDao;
import com.nki.t1.domain.Role;
import com.nki.t1.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.jsf.FacesContextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Qualifier("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private static final String ROLE_PREFIX = "ROLE_";
    private final UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("@@@@@ Start of CustomUserDetailsService_loadUserByUsername");
        // DB에서 Account 객체 조회
        UserDto userDto = userRepository.findById(username);
        System.out.println("----- userDto = " + userDto);
        if (userDto == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        // 권한 정보 등록
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_"+ userDto.getUserRole().name()));
        Collection<GrantedAuthority> authorities = roles;
        AccountContext accountContext = new AccountContext(userDto, authorities);
        System.out.println("----- accountContext = " + accountContext);
        // 권한을 담은 UserDetails 혹은 User 객체 반환
        System.out.println("@@@@@ End of CustomUserDetailsService_loadUserByUsername");
        return accountContext;
    }
}