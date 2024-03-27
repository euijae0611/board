package com.portfolio.board.Controller;

import com.portfolio.board.DTO.UserDTO;
import com.portfolio.board.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String save(@ModelAttribute UserDTO userDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (userService.isEmailExists(userDTO.getUserEmail())) {
            redirectAttributes.addFlashAttribute("emailExists", true);
            redirectAttributes.addFlashAttribute("userDTO", userDTO); // 사용자가 입력한 값들을 다시 뷰로 전달
            return "redirect:/users/register";
        }
        userService.save(userDTO);
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
