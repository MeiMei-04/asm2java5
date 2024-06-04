/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ASM2.controller;

import com.example.ASM2.auth.SessisonMaganer;
import com.example.ASM2.model.Customer;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.CustomerRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hieu
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    private List<Customer> customers = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @ModelAttribute("customers")
    public List<Customer> fillCustomer() {
        return customers = customerRepository.findAll();
    }
    @ModelAttribute("users")
    public List<User> fillUser() {
        return users = userRepository.findAll();
    }
    @GetMapping("/list")
    public String list() {
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        return "/customer/listCustomer.html";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("customer") Customer c){
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        return "/customer/addCustomer.html";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Customer c,BindingResult bindingResult, @RequestParam Long user_id,Model model){
        if(bindingResult.hasErrors()){
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String,String> errors  = new HashMap<>();
            for (FieldError fieldError1 : listError) {
                errors.put(fieldError1.getField(), fieldError1.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("customer", c);
            return "/customer/addCustomer.html";
        }
        System.out.println(user_id);
        User user = userRepository.findById(user_id).orElseThrow();
        Customer customer = new Customer();
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);
        return "redirect:/customer/list";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        Customer customer = customerRepository.findById(id).orElseThrow();
        model.addAttribute("customer", customer);
        return "/customer/editCustomer.html";
    }
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,@ModelAttribute @Valid Customer c,BindingResult bindingResult,@RequestParam Long user_id,Model model){
        if(bindingResult.hasErrors()){
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String,String> errors  = new HashMap<>();
            for (FieldError fieldError1 : listError) {
                errors.put(fieldError1.getField(), fieldError1.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("customer", c);
            return "/customer/editCustomer.html";
        }
        Customer customer = customerRepository.findById(id).orElseThrow();
        User user = userRepository.findById(user_id).orElseThrow();
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);
        return "redirect:/customer/list";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        if (!SessisonMaganer.isLogin()) {
            return "redirect:/login";
        }
        Customer customer = customerRepository.findById(id).orElseThrow();
        customerRepository.delete(customer);
        return "redirect:/customer/list";
    }
}
