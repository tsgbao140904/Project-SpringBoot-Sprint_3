package com.example.recipediscovery.service;

import com.example.recipediscovery.dto.LoginRequest;
import com.example.recipediscovery.dto.RegisterRequest;
import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.model.User;
import com.example.recipediscovery.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public SessionUser login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .filter(u -> u.getPassword().equals(req.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Sai email hoặc mật khẩu"));

        SessionUser su = new SessionUser();
        su.setId(user.getId());
        su.setFullName(user.getFullName());
        su.setEmail(user.getEmail());
        su.setRole(user.getRole());

        return su;
    }

    public User register(RegisterRequest req) {

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // plain text
        user.setRole("USER");

        return userRepo.save(user);
    }

    public void setSessionUser(SessionUser su, HttpSession session) {
        session.setAttribute("USER", su);
    }

    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
