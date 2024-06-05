/*@
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Hieu
 */
@Controller
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        logger.info("Accessing login page");
        return "redirect:";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        User u = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (u != null) {
            logger.info("User logged in successfully: " + u.getUsername());
            SessionManager.login(u);
            return "redirect:/home";
        }
        String textLogin = "Login FAIL";
        logger.warn("Login failed for user: " + user.getUsername());
        model.addAttribute("textLogin", textLogin);
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
