package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.Customer;
import com.example.ASM2.model.History;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.CustomerRepository;
import com.example.ASM2.repository.HistoryRepository;
import com.example.ASM2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

//    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    private HttpServletRequest request;

    @ModelAttribute("customers")
    public List<Customer> fillCustomer() {
        if (!SessionManager.isLogin()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            //logger.debug("User not logged in | IP: {}", ipAddress);
            return null;
        }
        return customerRepository.findAll();
    }

    @ModelAttribute("users")
    public List<User> fillUser() {
        if (!SessionManager.isLogin()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            return null;
        }
        return userRepository.findAll();
    }

    @GetMapping("/list")
    public String list(Model model) {
        if (!SessionManager.isLogin()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LOGIN PAGE"));
            //logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LIST CUSTOMER PAGE"));
        //logger.info("Accessing customer list");
        return "/customer/listCustomer.html";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("customer") Customer c) {
        if (!SessionManager.isLogin()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LOGIN PAGE"));
            //logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        //logger.info("Accessing add customer page");
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT ADD CUSTOMER PAGE"));
        return "/customer/addCustomer.html";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Customer c, BindingResult bindingResult, @RequestParam Long user_id, Model model) {
        if (bindingResult.hasErrors()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "VALIDATE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "VALIDATE: " + bindingResult.getFieldError()));
            //logger.error("Validation errors while saving customer: {}", bindingResult.getFieldError());
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("customer", c);
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT ADD CUSTOMER PAGE"));
            return "/customer/addCustomer.html";
        }
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = new Customer();
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "INSERT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "DATA| NAME: " + customer.getName() + " |EMAIL: " + customer.getEmail() + " |USER NAME: " + user.getName()));
        //logger.info("Customer saved successfully");
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LIST CUSTOMER PAGE"));
        return "redirect:/customer/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        if (!SessionManager.isLogin()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LOGIN PAGE"));
            //logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        model.addAttribute("customer", customer);
        //logger.info("Accessing edit page for customer ID: {}", id);
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT EDIT CUSTOMER PAGE BYID: " + SessionManager.getUserLogin().getId()));
        return "/customer/editCustomer.html";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute @Valid Customer c, BindingResult bindingResult, @RequestParam Long user_id, Model model) {
        if (bindingResult.hasErrors()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "VALIDATE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "VALIDATE: " + bindingResult.getFieldError()));
            //logger.error("Validation errors while updating customer: {}", bindingResult.getFieldError());
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("customer", c);
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT EDIT CUSTOMER PAGE BYID: " + SessionManager.getUserLogin().getId()));
            return "/customer/editCustomer.html";
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "UPDATE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "DATA| NAME: " + customer.getName() + " |EMAIL: " + customer.getEmail() + " |USER NAME: " + user.getName()));
        //logger.info("Customer updated successfully");
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LIST CUSTOMER PAGE"));
        return "redirect:/customer/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (!SessionManager.isLogin()) {
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "LOGIN", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "USER NOT LOGIN"));
            historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LOGIN PAGE"));
            //logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT DELETE CUSTOMER PAGE BYID: " + SessionManager.getUserLogin().getId()));
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "DELETE", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "DATA| NAME: " + customer.getName() + " |EMAIL: " + customer.getEmail()));
        //logger.info("Customer deleted successfully");
        historyRepository.save(new History(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr(), "REDIRECT", SessionManager.getUserLogin() == null ? null : SessionManager.getUserLogin().getUsername(), "REDIECT LIST CUSTOMER PAGE"));
        return "redirect:/customer/list";
    }
}
