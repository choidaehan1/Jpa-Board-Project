package com.example.project.controller;

import com.example.project.dto.LoginDto;
import com.example.project.dto.UserDto;
import com.example.project.entity.User;
import com.example.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/user/login";
    }

    @GetMapping("/user/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/user/register")
    public String signUpUser(UserDto userDto, Model model) {
        User savedUser = userService.saveEntity(userDto);
        if (savedUser != null) {
            model.addAttribute("message", "회원가입에 성공했습니다.");
            return "redirect:/user/login";
        } else {
            model.addAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "register";
        }
    }

    @GetMapping("/user/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/user/login")
    public String loginUser(LoginDto loginDto, Model model, HttpSession session) {
        boolean isAuthenticated = userService.login(loginDto.getUserId(), loginDto.getUserPw());
        if (isAuthenticated) {
            // 세션에 사용자 ID 저장
            session.setAttribute("userId", loginDto.getUserId());
            return "redirect:/board";
        } else {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
            return "login";
        }
    }

    @GetMapping("/user/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/user/login";
    }
}
