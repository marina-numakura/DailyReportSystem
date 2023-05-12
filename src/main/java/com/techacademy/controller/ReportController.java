package com.techacademy.controller;

import java.time.LocalDateTime;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;
@Controller
@RequestMapping("report")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("reportlist", service.getReportList());
        model.addAttribute("size", service.getReportList().size()); //　全件数を渡す
        // report/list.htmlに画面遷移
        return "report/list";
    }

    /** 詳細画面を表示 */
    @GetMapping(value = { "/detail", "/detail/{id}/" })
    public String getReport(@PathVariable(name = "id", required = false) Integer id,@AuthenticationPrincipal UserDetail userDetail,Model model) {
        // idが指定されていたら検索結果、無ければ空のクラスを設定
        Report report = id != null ? service.getReport(id) : new Report();
        // Modelに登録
        model.addAttribute("report", report);
        int flag = 0; // フラグ変数を0
        // 日報を書いた人とログインしている人の従業員情報のidを比較
        if (report.getEmployee().getId()==userDetail.getEmployee().getId()) {
            flag = 1; // フラグ１の時は従業員情報が一致した時
        }
        model.addAttribute("flag",flag); // 画面側にflag変数を渡して0か1比較
        // 日報詳細画面（report/detail.html）に遷移
        return "report/detail";
    }

    /** 日報情報新規登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Report report,@AuthenticationPrincipal UserDetail userDetail,Model model) {
     // Modelに登録
        model.addAttribute("userDetail", userDetail);
        // 日報登録画面（report/register.html）に遷移
        return "report/register";
    }


    /** 日報新規登録処理 */
    @PostMapping("/register")
    public String postRegister(Report report,@AuthenticationPrincipal UserDetail userDetail) {
        // タイトルまたは内容が空欄だった場合
        if(report.getTitle().equals("") ||
                report.getContent().equals("")) {
            // 日報登録画面（report/register.html）に遷移
            return "report/register";
        }
        report.setEmployee(userDetail.getEmployee());
        LocalDateTime currentTime = LocalDateTime.now(); // 現在日時
        report.setCreatedAt(currentTime); //　登録日時
        report.setUpdatedAt(currentTime); //　更新日時
        try {
            // 日報登録
            service.saveReport(report);
        } catch (Exception e) { //エラーが起こった場合登録画面に戻る　※Exception（全エラーを受け取る）
            return "report/register";
        }
        // 一覧画面（report/list.html）にリダイレクト
        return "redirect:/report/list";
    }

    /** 日報情報編集画面を表示 */
    @GetMapping(value = {"/edit","/edit/{id}/" })
    public String getReportedit(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // idが指定されていたら検索結果、無ければ空のクラスを設定
        Report report = id != null ? service.getReport(id) : new Report();
        // Modelに登録
        model.addAttribute("report", report);
        // 日報詳細画面（report/edit.html）に遷移
        return "report/edit";
    }

    /** 日報情報の編集処理(更新) */
    @PostMapping("/edit/{id}/")
    public String postReport(@PathVariable(name = "id", required = false) Integer id, Report report) {
        Report tableReport = id != null ? service.getReport(id) : new Report();
        tableReport.setReportDate(report.getReportDate()); //　日報の日付上書き
        tableReport.setTitle(report.getTitle()); //　タイトル上書き
        tableReport.setContent(report.getContent()); //　内容上書き
        LocalDateTime currentTime = LocalDateTime.now();
        tableReport.setUpdatedAt(currentTime); // 更新日時
        // 日報情報登録
        service.saveReport(tableReport);
        // 一覧画面（report/list.html）にリダイレクト
        return "redirect:/report/list";
    }
}
