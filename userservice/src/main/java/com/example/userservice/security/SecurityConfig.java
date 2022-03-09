package com.example.userservice.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     *
     * 스프링 시큐리티는 지극히 유연, 어떠한 데이터 저장소에서도 가상으로
     * 사용자 인증이 가능하다.
     *
     * 몇 가지 일반적인 사용자 저장 방식인 인메모리, 관계형 DB 등
     *
     * WebSecurityConfigurerAdapter 를 확장하여 보안 설정을 했음느로
     * 가장 쉽게 사용자 저장소를 설정하는 방법은,
     * AuthenticationManagerBuilder 를 인자로 갖는 configure() 메소드를
     * 오버라이딩 하는 것!
     *
     * AuthenticationManagerBuilder 는 스프링 시큐리티의 인증에 대한
     * 지원을 설정하는 몇가지 메소드가 있는데,
     * inMemoryAuthentication() 메소드로 활성화 및 설정이 가능하고
     * 선택적으로 인메모리 사용자 저장소에 값을 채울 수 있다.
     *
     * There are many different ways you can tell Spring "how to look for the users".
     * First one is "in memory".
     * So i can select in memory, And then i pass a username and password
     * so that Spring can use check for users whenever users are trying to login to the application.
     *
     * BUT this time, we will use userDetailService
     *
     *
     */
    // Spring will inject this guys
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
}
