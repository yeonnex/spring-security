package com.example.userservice.filter;

import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Whenever the user tries to login
 *
 * We want to be able to send the "access token" and the "refresh token" whenever the login is successful!
 * -> Override the successfulAuthenticationn 메소드! which is called when the login is successful.
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // Calling the authenticationManager to authenticate the user
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // can see the log whenever the user is trying to login.
        log.info("Username is: {}", username);
        log.info("Password id: {}", password);

        // Need to create an object of usernamePasswordAuthentication token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // 위 함수를 통해 인증이 성공하면, 이 함수가 실행되게 된다다
   @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    /**
     * TODO unsuccessfulAuthentication 메서드도 있는데,
     * 이 메서드가 몇번 호출됐냐에 따라 강제로 얼마 시간동안 로그인 못하게 커스텀할 수 있음
     */
}
