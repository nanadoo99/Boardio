package com.nki.t1.utils;

import com.nki.t1.domain.UserIdentifiable;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.domain.Role;
import com.nki.t1.dto.UserDto;
import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.exception.InvalidAuthenticationException;
import com.nki.t1.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class SessionUtils {

    public static UserDto getUserDto(HttpServletRequest request) throws UnauthorizedException {

        // 사용 - UserDto userDto = sessionUtils.getUserDto(request);
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("authUser");
        log.info("===== sessionUtils.getUserDto(request) obj={}", obj.toString());
        if (obj != null && obj instanceof UserSecurityDto) {
            log.info("===== sessionUtils.getUserDto if true");
            return ((UserSecurityDto) obj).toUserDto();
        } else {
            throw new InvalidAuthenticationException(ErrorType.NOT_AUTHENTICATED);
        }
    }

    public static UserDto getUserDto() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // principal 객체 가져오기
        Object principal = authentication.getPrincipal();
        log.info("===== sessionUtils.getUserDto() principal={}", principal.toString());

        if (principal instanceof UserDetails) {
            return ((UserSecurityDto) principal).toUserDto();
        } else {
            throw new InvalidAuthenticationException(ErrorType.NOT_AUTHENTICATED);
        }
    }

    public static UserSecurityDto getUserSecurityDto() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // principal 객체 가져오기
        Object principal = authentication.getPrincipal();
        log.info("===== sessionUtils.getUserDto() principal={}", principal.toString());

        if (principal instanceof UserDetails) {
            return (UserSecurityDto) principal;
        } else {
            throw new InvalidAuthenticationException(ErrorType.NOT_AUTHENTICATED);
        }
    }

    public static void setRefererURL(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String refererURL = "/";
        
        // 로그인 혹은 회원가입
        if (referer != null && !referer.contains("/login") && !referer.contains("/signup")) {
            refererURL = referer;
        }

        HttpSession session = request.getSession();
        session.setAttribute("refererURL", refererURL);
        session.setMaxInactiveInterval(300); // 세션 유효 시간 설정 (5분)
        log.info("----- session refererURL : " + refererURL);
    }

    public static String getRefererURL(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String refererURL = "/";
        Object refererObj = session.getAttribute("refererURL");
        if (refererObj != null && refererObj instanceof String) {
            refererURL = (String) refererObj;
            session.removeAttribute("refererURL"); // 값이 한 번 사용되면 삭제
        }

        log.info("----- getRefererURL : " + refererURL);
        return refererURL;
    }

    public static void setRedirectURI(HttpServletRequest request, String uri) {
        HttpSession session = request.getSession();
        session.setAttribute("redirectURI", uri);
        session.setMaxInactiveInterval(300); // 세션 유효 시간 설정 (5분)
        log.info("----- session redirectURI : " + uri);
    }

    public static String getRedirectURI(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String redirectURI = "/";
        Object redirectURIObj = session.getAttribute("redirectURI");
        if (redirectURIObj != null && redirectURIObj instanceof String) {
            redirectURI = (String) redirectURIObj;
            session.removeAttribute("redirectURI"); // 값이 한 번 사용되면 삭제
        }

        log.info("----- getRedirectURI : " + redirectURI);
        return redirectURI;
    }

    // 일반 유저 작성자 혹은 관리자
    public static void checkAdminOrWriter(UserIdentifiable userIdentifiable) throws UnauthorizedException {
        log.debug("----- SessionUtils checkAdminOrWriter");
//        UserDto userDto = getUserDto(request);
        UserDto userDto = getUserDto();

        boolean isAdmin = Role.ADMIN.equals(userDto.getUserRole());
        boolean isWriter = userIdentifiable.getUno() != null && userIdentifiable.getUno().equals(userDto.getUno());
        boolean isSuperAdmin = Role.SUPER_ADMIN.equals(userDto.getUserRole());

        log.info("----- isAdmin:" + userDto.getUserRole());
        log.info("----- userIdentifiable.getUno():" + userIdentifiable.getUno());
        log.info("----- userDto.getUno():" + userDto.getUno());

        // 일반 유저 작성자 혹은 관리자
        if (!((isAdmin || isWriter) || isSuperAdmin)) {
            log.debug("----- UnauthorizedException: Not admin or writer");
            throw new UnauthorizedException(ErrorType.REQUEST_UNAUTH);
        }
        log.debug("----- Authorized");
        log.info("------------------------------------");

    }

    // 작성한 관리자 혹은 수퍼 관리자 - 주로 공지사항
    public static void checkAdminAndWriter(UserIdentifiable userIdentifiable) throws UnauthorizedException {
        log.debug("----- SessionUtils checkAdminAndWriter");
        UserSecurityDto userDto = getUserSecurityDto();
        log.info("----- userDto={}", userDto.toString());

        boolean isAdmin = Role.ADMIN.equals(userDto.getUserRole());
        boolean isWriter = userIdentifiable.getUno() != null && userIdentifiable.getUno().equals(userDto.getUno());
        boolean isSuperAdmin = Role.SUPER_ADMIN.equals(userDto.getUserRole());
        log.info("----- role={}", userDto.getUserRole());
        log.info("----- session uno={}", userDto.getUno());
        log.info("----- userIdentifiable.getUno():" + userIdentifiable.getUno());

        // 작성한 관리자 혹은 수퍼 관리자 - 주로 공지사항
        if (!((isAdmin && isWriter) || isSuperAdmin)) {
            log.debug("----- UnauthorizedException: Not admin and writer");
            throw new UnauthorizedException(ErrorType.REQUEST_UNAUTH);
        }
    }

}