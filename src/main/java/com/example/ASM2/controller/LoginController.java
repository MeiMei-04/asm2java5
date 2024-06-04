/*@
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ASM2.controller;

import com.example.ASM2.auth.SessisonMaganer;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "redirect:";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user,Model model) {
        User u = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (u != null) {
            SessisonMaganer.login(u);
            return "redirect:/home";
        }
        String textLogin = "Login FAIL";
        model.addAttribute("textLogin", textLogin);
        return "/index.html";
    }

    @GetMapping("/home")
    public String home() {
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        return "/login/home.html";
    }
    @GetMapping("/logout")
    public String logout(){
        SessisonMaganer.logout();
        return "redirect:/";
    }
}
