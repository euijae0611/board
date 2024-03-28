package com.portfolio.board.Config;

import com.portfolio.board.Service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityUserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인가 설정
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/").permitAll() // 변경된 부분
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/member/**").hasAnyRole("ADMIN", "MEMBER")
                                .requestMatchers("/users/loginSuccess").hasAnyRole("USER")
                )
                .csrf(csrf -> csrf.disable()) // 변경된 부분

                // 로그인 설정
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/users/login") // 사용자 정의 로그인 페이지 경로
                                .loginProcessingUrl("/users/login") // 로그인 처리 경로. Spring Security가 이 경로로 오는 POST 요청을 처리
                                .defaultSuccessUrl("/", true) // 로그인 성공 시 리다이렉션 경로
                                .failureUrl("/users/login?error=true") // 로그인 실패 시 리다이렉션 경로
                                .usernameParameter("userEmail") // 로그인 폼에서 사용되는 input name for username
                                .passwordParameter("userPW") // 로그인 폼에서 사용되는 input name for password
                )

                // 로그아웃 설정
                .logout(logout ->
                        logout
                                .invalidateHttpSession(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                                .logoutSuccessUrl("/users/login?logout=true")
                )

                // 사용자 인증 처리 컴포넌트 서비스 등록
                .userDetailsService(userService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
