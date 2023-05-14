package com.techacademy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

    /** ログアウト処理を行なう */
    @PostMapping("/logout")
    public String postLogout() {
        // ログイン画面にリダイレクト
        return "redirect:/login";
    }

    /** ログアウト処理を行なう */
    @GetMapping("/logout")
    public String getLogout() {
        // ログイン画面にリダイレクト
        return "redirect:/login";
    }

}