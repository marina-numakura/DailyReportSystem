package com.techacademy.controller;

import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Authentication;
import com.techacademy.entity.Employee;
import com.techacademy.service.EmployeeService;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("employeelist", service.getEmployeeList());
        // employee/list.htmlに画面遷移
        return "employee/list";
    }

    /** 詳細画面を表示 */
    @GetMapping(value = { "/detail", "/detail/{id}/" })
    public String getEmployee(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のクラスを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // employee/detail.htmlに画面遷移
        return "employee/detail";
    }

    /** 従業員新規登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Employee employee) {
        // 従業員登録画面に遷移
        return "employee/register";
    }

    /** 従業員登録処理 */
    @PostMapping("/register")
    public String postRegister(Employee employee) {
        LocalDateTime currentTime = LocalDateTime.now();
        employee.setCreatedAt(currentTime);
        employee.setDeleteFlag(0);
        employee.setUpdatedAt(currentTime);
        Authentication a = employee.getAuthentication();
        a.setEmployee(employee);
        // 従業員登録
        service.saveEmployee(employee);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

    /** 従業員情報　編集ページを表示 */
    @GetMapping(value = {"/edit","/edit/{id}/" })
    public String getEmployeeedit(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のクラスを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // employee/edit.htmlに画面遷移
        return "employee/edit";
    }

    /** 従業員情報の編集処理 */
    @PostMapping("/edit")
    public String postEdit(Employee employee) {
        // 従業員情報登録
        service.saveEmployee(employee);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

    /** 従業員情報の削除処理 */
    @GetMapping(value = {"/delete","/delete/{id}/" })
    public String getEmployeedelete(@PathVariable(name = "id", required = false) Integer id, Model model) {
        service.removeEmployee(id);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }
}