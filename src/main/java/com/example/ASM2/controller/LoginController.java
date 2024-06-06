package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.History;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.HistoryRepository;
import com.example.ASM2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This class handles login, logout, and home page access for the application.
 *
 * @author Hieu (author of the original code)
 */
@Controller
public class LoginController {

//    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    HttpServletRequest request;

    @GetMapping("/login")
    public String login() {
        //logger.info("Accessing login page");
        // Consider returning a specific login view instead of redirect
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LOGIN PAGE"));
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        User authenticatedUser = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            //logger.info("User logged in successfully: " + authenticatedUser.getUsername());
            SessionManager.login(authenticatedUser);
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER LOGIN"));
            return "redirect:/home";
        }
        String loginFailText = "Login failed";
        //logger.warn("Login failed for user: " + user.getUsername());
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", user.getUsername(), "USER LOGIN FAIL"));
        model.addAttribute("textLogin", loginFailText);
        return "/index.html";
    }

    @GetMapping("/home")
    public String home() {
        if (!SessionManager.isLogin()) {
            //logger.warn("Access denied: user not logged in");
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            return "redirect:/login";
        }
        //logger.info("Accessing home page");
         historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT HOME PAGE"));
        return "/login/home.html";
    }

    @GetMapping("/logout")
    public String logout() {
        //logger.info("User logged out");
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGOUT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER LOGOUT"));
        SessionManager.logout();
        return "redirect:/";
    }
}
