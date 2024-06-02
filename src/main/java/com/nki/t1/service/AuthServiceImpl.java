package com.nki.t1.service;

import com.nki.t1.dao.UserDao;
import com.nki.t1.domain.LoginResult;
import com.nki.t1.dto.UserDto;
import com.nki.t1.security.MyCustomAuthenticationSuccessHandler;
import com.nki.t1.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private MyCustomAuthenticationSuccessHandler myCustomAuthenticationSuccessHandler;

    @Autowired
    private final UserDetailsService userDetailsService;

    public LoginResult loginPwChk(UserDto userDto) {
        UserDto userInfo = userDao.selectUserById(userDto);
        if(userInfo!=null) {
            return new LoginResult(true,userInfo);
        }
        LoginResult result = new LoginResult();
        result.setSuccess(false);
        return result;
    }

    @Override
    @Transactional
    public int register(UserDto userDto, HttpServletRequest request) throws RuntimeException {
        int result = 0;

        try {
            boolean existMember = userDao.existMember(userDto);

            if(existMember) {
                System.out.println("@@@@@ register_existMember");
                throw new RuntimeException("existMember");}

            UserDto insertUser = new UserDto(userDto);
            insertUser.encryptPassword(passwordEncoder);
            System.out.println("post encrypt userDto = " + insertUser);

//        return userDao.insertUser(insertUser);
            result = userDao.insertUser(insertUser);
            if(result==0) {throw new RuntimeException("insert user failed");}

            Authentication autoAuthentication = autoAuthentication(userDto, request);
            myCustomAuthenticationSuccessHandler.setUserInSession(request, autoAuthentication);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Authentication autoAuthentication(UserDto userDto, HttpServletRequest request) throws RuntimeException {

        String username = userDto.getId();
        String password = userDto.getPassword();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        Authentication authentication;
        System.out.println("---- autoLogin 1 -----");
        try {
            // 사용자의 자격 증명을 인증
            authentication = authenticationManager.authenticate(authToken);
            System.out.println("---- authentication = " + authentication);
            System.out.println("---- autoLogin 2 -----");

            // SecurityContext에 인증된 사용자 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

//            Object principal = authentication.getPrincipal();
//            if (principal instanceof UserDto) {
//                UserDto userDto2 = (UserDto) principal;
//                System.out.println("----- userDto2 =" + userDto2);
//                String userId = userDto2.getId();  // UserDetailsImpl 에서 getUsername() > return member.getId()
//                HttpSession session = request.getSession();
//                if(userId != null || !userId.isEmpty()) {
//                    session.setAttribute("user", userDto2);
//                }
//            }

        } catch (Exception e) {
            // 인증 실패 처리
            throw new RuntimeException(e);
        }

        return authentication;
    }

    @Override
    public int idChk(String id) {
        return userDao.idChk(id);
    }
}
