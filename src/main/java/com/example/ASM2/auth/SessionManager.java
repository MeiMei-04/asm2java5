/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ASM2.auth;

import com.example.ASM2.model.User;

/**
 *
 * @author Hieu
 */
public class SessionManager {
    private static boolean isLogin = false;
    private static User user = null;
    public static void login(User user){
        SessionManager.isLogin = true;
        SessionManager.user = user;
    }
    public static User getUserLogin(){
        return SessionManager.user;
    }
    public static boolean isLogin(){
        return SessionManager.isLogin;
    }
    public static void logout(){
        SessionManager.isLogin = false;
        SessionManager.user = null;
    }
}
