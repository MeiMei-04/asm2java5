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
public class SessisonMaganer {
    private static boolean isLogin = false;
    private static User user = null;
    public static void login(User user){
        SessisonMaganer.isLogin = true;
        SessisonMaganer.user = user;
    }
    public static User getUserLogin(){
        return SessisonMaganer.user;
    }
    public static boolean isLogin(){
        return SessisonMaganer.isLogin;
    }
    public static void logout(){
        SessisonMaganer.isLogin = false;
        SessisonMaganer.user = null;
    }
}
