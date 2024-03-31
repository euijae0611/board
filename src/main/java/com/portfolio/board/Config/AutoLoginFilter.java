package com.portfolio.board.Config;

import com.portfolio.board.DTO.UserDTO;
import com.portfolio.board.Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class AutoLoginFilter implements Filter {

    private final UserService userService;

    public AutoLoginFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // "autoLogin" 쿠키 확인
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("autoLogin".equals(cookie.getName()) && cookie.getValue() != null) {
                    // 쿠키에서 사용자 식별 정보(이메일) 추출
                    String userEmail = cookie.getValue();
                    // 사용자 인증 로직 (UserService 사용)
                    UserDTO userDTO = userService.autoLogin(userEmail);
                    if (userDTO != null) {
                        // 여기서는 예시로 사용자 이메일을 세션에 저장
                        httpServletRequest.getSession().setAttribute("loginEmail", userDTO.getUserEmail());
                        break;
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}
