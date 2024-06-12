package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.History;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.HistoryRepository;
import com.example.ASM2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Hieu (author of the original code)
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    HttpServletRequest request;
    private List<User> users = new ArrayList<>(); // Consider using UserRepository instead

    @ModelAttribute("users")
    public List<User> fillAll() {
        users = userRepository.findAll();
        return users;
    }

    @GetMapping("/list")
    public String list() {
        if (!SessionManager.isLogin()) {
            return "redirect:/login";
        }
        return "/user/listUser.html";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") User user) {
        if (!SessionManager.isLogin()) {
            return "redirect:/login";
        }
        return "/user/addUser.html";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "/user/addUser.html";
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
        }
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()),
                request.getRemoteAddr(),
                "UPDATE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(),
                "DATA={" + user.getId() + ";" + user.getName() + ";" + user.getPassword() + ";" + user.getPassword() + "}"));
        return "redirect:/user/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        if (!SessionManager.isLogin()) {
            return "redirect:/login";
        }
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("User not found");
        });
        model.addAttribute("user", user);
        return "/user/editUser.html";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute @Valid User u, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", u);
            return "/user/addUser.html";
        }

        User user = userRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("User not found");
        });
        user.setName(u.getName());
        user.setPassword(u.getPassword());
        user.setUsername(u.getUsername());
        try {
            userRepository.save(user);
        } catch (Exception e) {
        }
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()),
                request.getRemoteAddr(),
                "UPDATE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(),
                "DATA={" + user.getId() + ";" + user.getName() + ";" + user.getPassword() + ";" + user.getPassword() + "}"));
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (!SessionManager.isLogin()) {
            return "redirect:/login";
        }
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("User not found");
        });
        try {
            userRepository.delete(user);
        } catch (Exception e) {
        }
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()),
                request.getRemoteAddr(),
                "DELETE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(),
                "DATA={" + user.getId() + ";" + user.getName() + ";" + user.getPassword() + ";" + user.getPassword() + "}"));
        return "redirect:/user/list"; // Consider returning a specific page instead of redirecting again
    }
}
