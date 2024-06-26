package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.History;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.HistoryRepository;
import com.example.ASM2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @Autowired
    UserRepository userRepository;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    HttpServletRequest request;

    @GetMapping("")
    public String index(@ModelAttribute("user") User u) {
        return "/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        boolean flag = false;
        if (bindingResult.hasErrors()) {
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                if (fieldError.getField().equals("name")) {
                    flag = true;
                }
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            if (flag) {
                User authenticatedUser = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
                if (authenticatedUser != null) {
                    SessionManager.login(authenticatedUser);
                    historyRepository.save(new History(new Timestamp(System.currentTimeMillis()),
                            request.getRemoteAddr(),
                            "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(),
                            "USER LOGIN"));
                    return "redirect:/home";
                }
                String loginFailText = "Login failed";
                model.addAttribute("textLogin", loginFailText);
            }
        }
        return "/index.html";

    }

    @GetMapping("/home")
    public String home() {
        if (!SessionManager.isLogin()) {
            return "redirect:/login";
        }
        return "/login/home.html";
    }

    @GetMapping("/logout")
    public String logout() {
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGOUT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER LOGOUT"));
        SessionManager.logout();
        return "redirect:/";
    }
}
