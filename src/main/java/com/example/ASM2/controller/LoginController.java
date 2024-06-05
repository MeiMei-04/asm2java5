package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This class handles login, logout, and home page access for the application.
 *
 * @author Hieu (author of the original code)
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        logger.info("Accessing login page");
        // Consider returning a specific login view instead of redirect
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        User authenticatedUser = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            logger.info("User logged in successfully: " + authenticatedUser.getUsername());
            SessionManager.login(authenticatedUser);
            return "redirect:/home";
        }
        String loginFailText = "Login failed";
        logger.warn("Login failed for user: " + user.getUsername());
        model.addAttribute("textLogin", loginFailText);
        return "/index.html";
    }

    @GetMapping("/home")
    public String home() {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        logger.info("Accessing home page");
        return "/login/home.html";
    }

    @GetMapping("/logout")
    public String logout() {
        logger.info("User logged out");
        SessionManager.logout();
        return "redirect:/";
    }
}