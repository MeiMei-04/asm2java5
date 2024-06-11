/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ASM2.controller;

import com.example.ASM2.auth.SessionManager;
import com.example.ASM2.model.History;
import com.example.ASM2.repository.HistoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author HieuCute
 */
@Controller
@RequestMapping("history")
public class HistoryController {
    @Autowired
    HistoryRepository historyRepository;
    private List<History> historys = new ArrayList<>();
    @ModelAttribute("historys")
    public List<History> getListHistory(){
        return  historys = historyRepository.findAll();
    }
    @GetMapping("/list")
    public String list(){
        if (!SessionManager.isLogin()) {
            return "redirect:/login";
        }
        return "/history/listHistory.html";
    }
}
