package com.portfolio.board.Controller;

import com.portfolio.board.DTO.UserDTO;
import com.portfolio.board.Service.EmailService;
import com.portfolio.board.Service.UserService;
import com.portfolio.board.Service.VerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String save(@ModelAttribute UserDTO userDTO,
                       @RequestParam("phonePrefix") String phonePrefix,
                       @RequestParam("phoneMiddle") String phoneMiddle,
                       @RequestParam("phoneLast") String phoneLast,
                       RedirectAttributes redirectAttributes) {
        if (userService.isEmailExists(userDTO.getUserEmail())) {
            userDTO.setUserEmail(""); // 이메일 필드만 초기화
            redirectAttributes.addFlashAttribute("emailExists", true);
            redirectAttributes.addFlashAttribute("userDTO", userDTO); // 변경된 userDTO 전달
            return "redirect:/users/register";
        }
        userService.save(userDTO, phonePrefix, phoneMiddle, phoneLast);
        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String dologin(@ModelAttribute UserDTO userDTO, @RequestParam(value = "remember-me", required = false) String rememberMe, HttpSession session, HttpServletResponse response) {
        UserDTO loginResult = userService.login(userDTO);
        if(loginResult != null) {
            session.setAttribute("loginEmail",loginResult.getUserEmail());
            session.setAttribute("loginName",loginResult.getUserName());
            // 자동 로그인 체크박스 확인
            if ("true".equals(rememberMe)) {
                Cookie autoLoginCookie = new Cookie("autoLogin", loginResult.getUserEmail());
                autoLoginCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간 설정
                autoLoginCookie.setPath("/");
                response.addCookie(autoLoginCookie);
            }
            return "redirect:/";
        } else {

            return "redirect:/users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 로그아웃 시 자동 로그인 쿠키 삭제
        Cookie autoLoginCookie = new Cookie("autoLogin", null);
        autoLoginCookie.setMaxAge(0);
        autoLoginCookie.setPath("/");
        response.addCookie(autoLoginCookie);

        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String userEmail) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = userService.isEmailExists(userEmail);
        response.put("exists", exists);
        if (!exists) {
            verificationService.sendAndSaveVerificationCode(userEmail);
        }
        return ResponseEntity.ok(response);
    }

}
