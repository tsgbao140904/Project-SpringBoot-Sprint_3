package com.example.recipediscovery.controller.auth;

import com.example.recipediscovery.dto.LoginRequest;
import com.example.recipediscovery.dto.RegisterRequest;
import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // GET /login
    @GetMapping({"/", "/login"})
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginRequest());
        return "auth/login";
    }

    // POST /login
    @PostMapping("/login")
    public String handleLogin(
            @Valid @ModelAttribute("login") LoginRequest loginRequest,
            HttpSession session,
            Model model
    ) {
        try {
            SessionUser su = authService.login(loginRequest);
            authService.setSessionUser(su, session);

            if ("ADMIN".equals(su.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/app/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    // GET /register
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("req", new RegisterRequest());
        return "auth/register";
    }

    // POST /register
    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("req") RegisterRequest request,
            Model model
    ) {
        try {
            authService.register(request);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    // POST /logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/login?logout";
    }
}
