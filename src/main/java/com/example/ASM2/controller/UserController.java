package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;
    private List<User> users = new ArrayList<>(); // Consider using UserRepository instead

    @ModelAttribute("users")
    public List<User> fillAll() {
        users = userRepository.findAll();
        logger.debug("Fetched all users: {}", users.size());
        return users;
    }

    @GetMapping("/list")
    public String list() {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        logger.info("Accessing user list page");
        return "/user/listUser.html";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") User user) {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        logger.info("Accessing add user page");
        return "/user/addUser.html";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors while saving user: {}", bindingResult.getFieldErrors());
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "/user/addUser.html";
        }
        userRepository.save(user);
        logger.info("User saved successfully: {}", user.getUsername());
        return "/user/listUser.html";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found");
        });
        model.addAttribute("user", user);
        logger.info("Accessing edit page for user ID: {}", id);
        return "/user/editUser.html";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute @Valid User u, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors while updating user: {}", bindingResult.getFieldErrors());
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
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found");
        });
        user.setName(u.getName());
        user.setPassword(u.getPassword());
        user.setUsername(u.getUsername());
        userRepository.save(user);
        logger.info("User updated successfully: {}", user.getUsername());
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found");
        });
        userRepository.delete(user);
        logger.info("User deleted successfully: {}", user.getUsername());
        return "redirect:/user/list"; // Consider returning a specific page instead of redirecting again
    }
}
