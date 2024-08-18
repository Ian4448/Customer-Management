package com.borges.project.api;

import com.borges.project.model.LoginForm;
import com.borges.project.service.CustomerDatabaseService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class CustomerLoginAPI {
    private final CustomerDatabaseService customerDatabaseService;

    @Autowired
    public CustomerLoginAPI(CustomerDatabaseService customerDatabaseService) {
        this.customerDatabaseService = customerDatabaseService;
    }


    @GetMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm,
                        @RequestParam(value = "token", required = false) String token,
                        Model model) {
        if (token != null && customerDatabaseService.isTrustedTokenValid(token)) {
            return "redirect:/home";
        }
        model.addAttribute("loginForm", loginForm);
        return "login";
    }

    /*
     * Todo(07/26/2024) add auth msg/token verification, add tracking to keep user as "trusted."
     * Verify that user login information exists/matches, then redirect
     * @throws CustomerRequestException if the login information is invalid
     * */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        HttpServletResponse response) {
        if (customerDatabaseService.hasLogin(loginForm)) {
            // Generate a new token
            String token = UUID.randomUUID().toString();
            customerDatabaseService.storeTrustedToken(loginForm.getUsername(), token);

            // Set token as a cookie
            Cookie cookie = new Cookie("auth_token", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
            response.addCookie(cookie);

            redirectAttributes.addFlashAttribute("loginSuccess", true);
            return "redirect:/home";
        }
        model.addAttribute("errorMessage", "Invalid login information");
        return "login";
    }
}
