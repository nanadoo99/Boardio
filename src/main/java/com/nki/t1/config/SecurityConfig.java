package com.nki.t1.config;

import com.nki.t1.security.CustomAuthenticationProvider;
import com.nki.t1.security.MyCustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyCustomAuthenticationSuccessHandler myCustomAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("@@@@@ Start of SecurityConfig_configure");

        http.csrf().disable();
        http.authorizeRequests()
                    .antMatchers("/public/login", "/public/signup").anonymous()
                    .antMatchers("/public/**", "/", "/static/**").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .antMatchers("/**").permitAll() // 테스트
                    // 나머지 모든 경로는 인증된 사용자만 접근 가능
                    .anyRequest().permitAll()
                .and()
                    .formLogin()                                    // form 을 통한 login 활성화
                    .loginPage("/public/login")                            // 로그인 페이지 URL 설정 , 설정하지 않는 경우 default 로그인 페이지 노출
                    .usernameParameter("id") // 아이디 파라미터명 설정
                    .successHandler(myCustomAuthenticationSuccessHandler)
                    .failureForwardUrl("/public/fail")                     // 로그인 실패 URL 설정
                .and()
                    .logout()
                    .logoutUrl("/public/logout")                           // 로그아웃 URL 설정
                    .logoutSuccessUrl("/");
        System.out.println("@@@@@ End of SecurityConfig_configure");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(authenticationProvider);
        return new ProviderManager(providers);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomAuthenticationProvider customProvider = new CustomAuthenticationProvider(); // 토큰 발행
        customProvider.setUserDetailsService(userDetailsService());
        customProvider.setPasswordEncoder(passwordEncoder());
        return customProvider;
    }

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public MyCustomAuthenticationSuccessHandler myCustomAuthenticationSuccessHandler() {
        MyCustomAuthenticationSuccessHandler handler = new MyCustomAuthenticationSuccessHandler();
        return handler;
    }

}