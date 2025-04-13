package com.nki.t1.security;

import com.nki.t1.dao.UserDao;
import com.nki.t1.domain.Role;
import com.nki.t1.dto.UserDto;
import com.nki.t1.dto.UserSecurityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest={}", userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        log.info("clientName={}", clientName);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();
        log.info("paramMap={}", paramMap);

        Map<String, String> details = new HashMap<>() {{
            put("email", "");
            put("id", "");
            put("access_token", accessToken);
        }};
        log.info("----- details={}", details);

        switch (clientName) {
            case "Google":
                details = getGoogleDetails(paramMap, details);
                break;
        }
        log.info("details={}", details);
        log.info("==========================================");


        return generateDto(details);
    }

    private Map<String, String> getGoogleDetails(Map<String, Object> paramMap, Map<String, String> details) {
        log.info("getGoogleEmail ----------------------------------");
        Map<String, String> result = new HashMap<>();
        details.replace("email", (String) paramMap.get("email"));
        details.replace("id", (String) paramMap.get("name"));
        String email = (String) paramMap.get("email");
        log.info("email={}", email);
        log.info("==========================================");
        return details;
    }

    private UserSecurityDto generateDto(Map<String, String> details) {
        // Optional.empty() 또는 값을 포함하는 Optional<UserDto> 반환
        Optional<UserDto> result = Optional.ofNullable(userDao.findByEmail(details.get("email")));
        UserDto userDto = null;


        if (result.isEmpty()) { // 해당 이메일로 가입된 계정이 없을 경우
            String userId = details.get("id").replaceAll("\\s", "");

            userDto = UserDto.builder()
                    .id(userId)
                    .email(details.get("email"))
                    .password(passwordEncoder.encode("1111"))
                    .authorities(List.of(new SimpleGrantedAuthority(Role.USER.getValue())))
                    .userRole(Role.USER)
                    .social(true).build();

            // 아이디가 존재하면 BATH아이디를 생성해 대입힌다.
            if (userDao.idChk(userId) > 0) {
                userDto.setId(generateUniqueUserIdBatch(userDto));
            }

            userDao.insertUser(userDto);

        } else { // 해당 이메일로 가입된 계정이 존재할 경우
            userDto = result.get();
            userDto.setSocial(true);
            userDao.updateUser(userDto);
            userDto.setAuthorities(List.of(new SimpleGrantedAuthority(userDto.getUserRole().getValue())));
        }

        log.info("userDto={}", userDto);
        UserSecurityDto userSecurityDto = getSecurityDto(userDto);
        userSecurityDto.setAccessToken(details.get("access_token")); // 여기
        log.info("userSecurityDto={}", userSecurityDto);

        autoAuthentication(userSecurityDto);
        return userSecurityDto;
    }

    private static UserSecurityDto getSecurityDto(UserDto userDto) {
        UserSecurityDto userSecurityDto = userDto.toUserSecurityDto();
        userSecurityDto.setUno(userDto.getUno());
        userSecurityDto.setUserRole(userDto.getUserRole());
        userSecurityDto.setSocial(userDto.isSocial());
        return userSecurityDto;
    }

    private void autoAuthentication(UserSecurityDto userSecurityDto) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userSecurityDto, null, userSecurityDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public String generateUniqueUserIdBatch(UserDto userDto) {
        String baseUserId = userDto.getId();
        String newUserId = baseUserId;

        while (true) {
            // 1. 이름 후보군 생성
            List<String> idCandidates = generateIdCandidates(newUserId, 50);

            // 2. Batch로 중복된 이름 조회
            List<String> existingId = userDao.existingSimilarIdList(newUserId);

            // 3. 존재하지 않는 이름을 선택
            for (String candidate : idCandidates) {
                if (!existingId.contains(candidate)) {
                    return candidate;
                }
            }

            // 4. 후보군이 모두 중복일 경우 새로운 ID 후보군을 생성
            newUserId = baseUserId + UUID.randomUUID().toString().substring(0, 5);
        }
    }

    // 이름 후보군 생성 메서드
    private List<String> generateIdCandidates(String baseName, int count) {
        List<String> candidates = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            candidates.add(baseName + i);
        }
        candidates.add(baseName + "_" + UUID.randomUUID().toString().substring(0, 5));
        return candidates;
    }
}
