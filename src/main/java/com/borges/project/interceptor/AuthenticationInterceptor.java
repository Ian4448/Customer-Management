package com.borges.project.interceptor;

import com.borges.project.service.CustomerDatabaseService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final CustomerDatabaseService customerDatabaseService;

    public AuthenticationInterceptor(CustomerDatabaseService customerDatabaseService) {
        this.customerDatabaseService = customerDatabaseService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (customerDatabaseService.isTrustedTokenValid(token)) {
                        return true;
                    }
                }
            }
        }
        response.sendRedirect("/login");
        return false;
    }
}
