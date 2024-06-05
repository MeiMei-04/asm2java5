package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.Customer;
import com.example.ASM2.model.User;
import com.example.ASM2.repository.CustomerRepository;
import com.example.ASM2.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
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

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = Logger.getLogger(CustomerController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private List<Customer> customers = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @ModelAttribute("customers")
    public List<Customer> fillCustomer() {
        customers = customerRepository.findAll();
        logger.debug("Fetched all customers: " + customers.size());
        return customers;
    }

    @ModelAttribute("users")
    public List<User> fillUser() {
        users = userRepository.findAll();
        logger.info("Fetched all users: " + users.size());
        return users;
    }

    @GetMapping("/list")
    public String list() {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        logger.info("Accessing customer list");
        return "/customer/listCustomer.html";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("customer") Customer c) {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        logger.info("Accessing add customer page");
        return "/customer/addCustomer.html";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Customer c, BindingResult bindingResult, @RequestParam Long user_id, Model model) {
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors while saving customer: " + bindingResult.getFieldErrors());
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("customer", c);
            return "/customer/addCustomer.html";
        }
        User user = userRepository.findById(user_id).orElseThrow(() -> {
            logger.error("User not found with ID: " + user_id);
            return new RuntimeException("User not found");
        });
        Customer customer = new Customer();
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);
        logger.info("Customer saved successfully: " + customer);
        return "redirect:/customer/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> {
            logger.error("Customer not found with ID: " + id);
            return new RuntimeException("Customer not found");
        });
        model.addAttribute("customer", customer);
        logger.info("Accessing edit page for customer ID: " + id);
        return "/customer/editCustomer.html";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute @Valid Customer c, BindingResult bindingResult, @RequestParam Long user_id, Model model) {
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors while updating customer: " + bindingResult.getFieldErrors());
            List<FieldError> listError = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : listError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("customer", c);
            return "/customer/editCustomer.html";
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> {
            logger.error("Customer not found with ID: " + id);
            return new RuntimeException("Customer not found");
        });
        User user = userRepository.findById(user_id).orElseThrow(() -> {
            logger.error("User not found with ID: " + user_id);
            return new RuntimeException("User not found");
        });
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);
        logger.info("Customer updated successfully: " + customer);
        return "redirect:/customer/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (!SessionManager.isLogin()) {
            logger.warn("Access denied: user not logged in");
            return "redirect:/login";
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> {
            logger.error("Customer not found with ID: " + id);
            return new RuntimeException("Customer not found");
        });
        customerRepository.delete(customer);
        logger.info("Customer deleted successfully: " + customer);
        return "redirect:/customer/list";
    }
}
