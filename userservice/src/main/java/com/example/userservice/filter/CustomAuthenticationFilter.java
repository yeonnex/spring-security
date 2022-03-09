package com.example.userservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

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

    // 위 함수를 통해 인증이 성공하면, 이 함수가 실행되게 된다
    // 여기서 access token 과 refresh 토큰 생성 예정
   @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // org.springframework.security.core.userdetails.User
        User user = (User)authentication.getPrincipal(); // Object 를 반환하기 때문. 이 로그인 성공한 유저 정보로 json 웹토큰을 만들어보자
       Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // TODO secret 값 .yml 파일에 저장하기
       String access_token = JWT.create()
               .withSubject(user.getUsername())
               .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 토큰의 만료시간은 10분
               .withIssuer(request.getRequestURL().toString())
               .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
               .sign(algorithm);
       String refresh_token = JWT.create()
               .withSubject(user.getUsername())
               .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 토큰의 만료시간은 10분
               .withIssuer(request.getRequestURL().toString())
//               .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())) // refresh 토큰의 경우 권한값을 안줘도 괜찮음
               .sign(algorithm);
       response.setHeader("access_token", access_token);
       response.setHeader("refresh_token", refresh_token);
    }

    /**
     * TODO unsuccessfulAuthentication 메서드도 있는데,
     * 이 메서드가 몇번 호출됐냐에 따라 강제로 얼마 시간동안 로그인 못하게 커스텀할 수 있음
     */
}
