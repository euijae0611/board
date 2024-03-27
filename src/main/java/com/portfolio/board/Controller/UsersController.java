package com.portfolio.board.Controller;

import com.portfolio.board.DTO.UserDTO;
import com.portfolio.board.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

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
    public String donelogin(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO loginResult = userService.login(userDTO);
        if(loginResult != null) {
            session.setAttribute("loginEmail",loginResult.getUserEmail());
            session.setAttribute("loginName",loginResult.getUserName());
            return "redirect:/";
        } else {

            return "redirect:/users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
