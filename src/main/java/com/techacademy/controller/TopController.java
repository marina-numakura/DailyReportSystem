package com.techacademy.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
public class TopController {

    private final ReportService service;

    public TopController(ReportService service) {
        this.service = service;
    }

    /** 自分の日報一覧を表示 */
    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetail userDetail,Model model) {
        model.addAttribute("reportlist", service.getReportList(userDetail.getEmployee()));
        model.addAttribute("size", service.getReportList(userDetail.getEmployee()).size()); //　全件数を渡す
        model.addAttribute("employee",userDetail.getEmployee()); //従業員情報を渡す
        return "index";
    }

}