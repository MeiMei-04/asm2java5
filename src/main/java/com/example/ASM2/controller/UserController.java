/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ASM2.controller;

import com.example.ASM2.auth.SessisonMaganer;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.UserRepository;
import jakarta.validation.Valid;
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
 * @author Hieu
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    private List<User> users = new ArrayList<>();
    @ModelAttribute("users")
    public List<User> fillAll(){
        return users = userRepository.findAll();
    }
    @GetMapping("/list")
    public String list() {
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        return "/user/listUser.html";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user")User user) {
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        return "/user/addUser.html";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute @Valid User user,BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String,String> errors  = new HashMap<>();
            for (FieldError fieldError1 : listError) {
                errors.put(fieldError1.getField(), fieldError1.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "/user/addUser.html";
        }
        userRepository.save(user);
        return "/user/listUser.html";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "/user/editUser.html";
    }
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute @Valid User u,BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String,String> errors  = new HashMap<>();
            for (FieldError fieldError1 : listError) {
                errors.put(fieldError1.getField(), fieldError1.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", u);
            return "/user/addUser.html";
        }
        User user = userRepository.findById(id).orElseThrow();
        user.setName(u.getName());
        user.setPassword(u.getPassword());
        user.setUsername(u.getUsername());
        userRepository.save(user);
        return "redirect:/user/list";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "/user/editUser.html";
    }
}
