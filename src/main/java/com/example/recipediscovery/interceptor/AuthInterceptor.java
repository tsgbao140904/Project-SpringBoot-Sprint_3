package com.example.recipediscovery.interceptor;

import com.example.recipediscovery.dto.SessionUser;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {

        String uri = req.getRequestURI();

        HttpSession session = req.getSession(false);
        SessionUser user = (session == null) ? null : (SessionUser) session.getAttribute("USER");

        // Cho phép public pages
        if (uri.equals("/") ||
                uri.startsWith("/login") ||
                uri.startsWith("/register") ||
                uri.startsWith("/css/")) {
            return true;
        }

        // Cấm nếu chưa đăng nhập
        if (user == null) {
            res.sendRedirect("/login");
            return false;
        }

        // Chặn admin nếu không phải admin
        if (uri.startsWith("/admin") && !"ADMIN".equals(user.getRole())) {
            res.sendRedirect("/app/home");
            return false;
        }

        return true;
    }
}
