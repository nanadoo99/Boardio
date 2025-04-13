package com.nki.t1.config;

import com.nki.t1.component.CustomAccessDeniedHandler;
import com.nki.t1.component.CustomAuthenticationEntryPoint;
import com.nki.t1.dao.UserDao;
import com.nki.t1.filter.ForwardedHeaderFilter;
import com.nki.t1.filter.PageTypeFilter;
import com.nki.t1.security.*;
import com.nki.t1.service.CustomUserDetailsService;
import com.nki.t1.utils.RedisUtil;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirct-uri}")
    private String redirectUri;

    private final UserDao userDao;
    private final SessionUtils sessionUtils;
    private final PageTypeFilter pageTypeFilter;
    private final RedisUtil redisUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(UserDao userDao,
                          SessionUtils sessionUtils,
                          PageTypeFilter pageTypeFilter,
                          RedisUtil redisUtil,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.userDao = userDao;
        this.sessionUtils = sessionUtils;
        this.pageTypeFilter = pageTypeFilter;
        this.redisUtil = redisUtil;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/websocket/**", "/health").permitAll() // 웹소켓 엔드포인트는 인증을 우회
                .antMatchers("/oauth2/**", "/auth/**", "/public/**", "/", "/static/**").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/superAdmin/**").hasRole("SUPER_ADMIN")
//                .anyRequest().permitAll() // 테스트
                .anyRequest().denyAll() // 인증된 사용자만 접근 가능

                .and()
                .oauth2Login()
                .loginPage("/auth/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService())
                .and()
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler())

                .and()
                .formLogin()
                .loginPage("/auth/login")
                .usernameParameter("email") // 아이디 파라미터명 설정
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler()) // 로그인 실패 URL 설정

                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")

                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint) // 비인증 사용자의 접근 처리
                .accessDeniedHandler(accessDeniedHandler); // 권한 부족 사용자의 접근 처리

        http.addFilterBefore(forwardedHeaderFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(pageTypeFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(authenticationProvider);
        return new ProviderManager(providers);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomAuthenticationProvider customProvider = new CustomAuthenticationProvider();
        customProvider.setUserDetailsService(userDetailsService(), passwordEncoder());
        return customProvider;
    }

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(redisUtil);
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userDao, passwordEncoder());
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    @Primary
    public CustomUserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userDao, passwordEncoder(), sessionUtils, customAuthenticationSuccessHandler());
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_SUPER_ADMIN > ROLE_ADMIN \n ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Collections.singletonList(googleClientRegistration()));
    }

    private ClientRegistration googleClientRegistration() {
        log.info("googleClientRegistration");
        return ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userNameAttributeName("email")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .redirectUriTemplate(redirectUri)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientName("Google")
                .build();
    }

}
