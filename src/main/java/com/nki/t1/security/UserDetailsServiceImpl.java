package com.nki.t1.security;

import com.nki.t1.dao.UserDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.domain.Role;
import com.nki.t1.dto.UserDto;
import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.exception.InvalidUserException;
import com.nki.t1.service.CustomUserDetailsService;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    @Value("${auth.max.attempts}")
    private Integer maxAttempts;

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private SessionUtils sessionUtils;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, SessionUtils sessionUtils, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.sessionUtils = sessionUtils;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws InvalidUserException {
        UserDto userDto = userDao.findByEmail(username);
        if (userDto == null) {
            throw new UsernameNotFoundException("cannot find user " + username);
        } else if (!userDto.isNonlocked()) {
            int failures = userDto.getFailures() + 1;
            userDto.setNonlocked(false);
            userDto.setFailures(failures);
            increaseFailures(userDto);
            throw new LockedException("" + failures);
        }
        return userDto.toUserSecurityDto();
    }

    @Override
    @Transactional
    public int register(UserDto userDto, HttpServletRequest request) throws RuntimeException {
        int result = 0;

        try {
            boolean existMemberId = userDao.existMemberId(userDto);
            boolean existMemberEmail = userDao.existMemberEmail(userDto);

            if (existMemberId || existMemberEmail) {
                throw new InvalidUserException(ErrorType.USER_REG_EXIST, userDto);
            }

            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userDto.setSocial(false);
            userDto.setUserRole(Role.USER);

            result = userDao.insertUser(userDto);
            if (result == 0) {
                throw new InvalidUserException(ErrorType.REQUEST_FAILED, userDto);
            }

            autoAuthentication(userDto, request);

        } catch (RuntimeException e) {
            throw e;
        }

        return result;
    }

    private void autoAuthentication(UserDto userDto, HttpServletRequest request) {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(userDto.getUserRole().getValue()));
        userDto.setAuthorities(list);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.toUserSecurityDto(), null, list);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        customAuthenticationSuccessHandler.setUserInSession(request, authenticationToken);
    }

    @Override
    public int idChk(String id) {
        return userDao.idChk(id);
    }

    @Override
    public int emailChk(String email) {
        return userDao.emailChk(email);
    }

    @Override
    public UserDto getUserDto(int uno) {
        return userDao.selectUserByUno(uno);
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto, HttpServletRequest request) throws RuntimeException {

        try {
            int result;
            UserSecurityDto authUser = sessionUtils.getUserSecurityDto();
            int uno = authUser.getUno();
            userDto.setUserRole(authUser.getUserRole());
            log.info("----- authUser : " + authUser + " uno : " + uno);
            userDto.setUno(uno);
            userDto.setEmail(authUser.getEmail());
            userDto.setSocial(authUser.isSocial());

            if (!authUser.isSocial()) {
                userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            } else {
                userDto.setPassword(userDao.findByEmail(userDto.getEmail()).getPassword());
            }
            result = userDao.updateUser(userDto);

            if (result == 0) {
                throw new InvalidUserException(ErrorType.REQUEST_FAILED);
            }

            autoAuthentication(userDto, request);

        } catch (DataAccessException e) {
            log.error("Database error occurred while updating user info", e);
            throw new InvalidUserException(ErrorType.REQUEST_FAILED, userDto);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while updating user info", e);
            throw new InvalidUserException(ErrorType.REQUEST_FAILED, userDto);
        }

    }

    @Override
    public void increaseFailures(UserDto userDto) {
        userDao.increaseFailures(userDto);
    }

    @Override
    public void resetFailures(int uno) {
        userDao.resetFailures(uno);
    }

    @Override
    public void checkFailure(String attemptPwd, UserDto userDto) throws LockedException, BadCredentialsException {
        if (!passwordEncoder.matches(attemptPwd, userDto.getPassword())) {
            log.info("----- unmatched password");
            log.info("----- userDto: " + userDto);
            int failures = userDto.getFailures() + 1;
            userDto.setFailures(failures);
            if (failures >= maxAttempts) { // 초과시
                log.info("----- LockedException");
                userDto.setNonlocked(false);
                userDao.increaseFailures(userDto);
                log.info("----- userDto: " + userDto);
                throw new LockedException("too many attempts");
            } else {
                log.info("----- BadCredentialsException");
                userDto.setNonlocked(true);
                userDao.increaseFailures(userDto);
                log.info("----- userDto: " + userDto);
                throw new BadCredentialsException("" + failures); // 보존할것.
            }
        }
    }

    @Override
    public void deactivate(HttpServletRequest request) {
        UserSecurityDto userSecurityDto = sessionUtils.getUserSecurityDto();

        // 관리자일 경우
        switch (userSecurityDto.getUserRole()) {
            case USER:
                    if (!userSecurityDto.isSocial()) { // 일반 로그인일 경우
                    } else { // 소셜 로그인일 경우
                        deactivateGoogle(userSecurityDto.getAccessToken());
                    }
                userDao.deleteUserByUno(userSecurityDto.toUserDto());
                request.getSession(false).invalidate();
                break;
            case ADMIN:
                userDao.deleteAdminByUno(userSecurityDto.toUserDto());
                request.getSession(false).invalidate();
                break;
            case SUPER_ADMIN:
                throw new InvalidUserException(ErrorType.REQUEST_UNAUTH);
        }
    }

    private void deactivateGoogle(String accessToken) {
        try {
            // 구글 API로 연동 해제 요청
            URI uri = new URI("https://oauth2.googleapis.com/revoke?token=" + accessToken);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, null, Object.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
