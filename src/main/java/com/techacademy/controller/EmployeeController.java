package com.techacademy.controller;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(@AuthenticationPrincipal UserDetail userDetail,Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("employeelist", service.getEmployeeList());
        model.addAttribute("size", service.getEmployeeList().size()); //　全件数を渡す
        model.addAttribute("employee",userDetail.getEmployee()); //従業員情報を渡す
        // employee/list.htmlに画面遷移
        return "employee/list";
    }

    /** 詳細画面を表示 */
    @GetMapping(value = { "/detail", "/detail/{id}/" })
    public String getEmployee(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // idが指定されていたら検索結果、無ければ空のクラスを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // 従業員詳細画面（employee/detail.html）に遷移
        return "employee/detail";
    }

    /** 従業員情報新規登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Employee employee) {
        // 従業員登録画面（employee/register.html）に遷移
        return "employee/register";
    }

    /** 従業員新規登録処理 */
    @PostMapping("/register")
    public String postRegister(Employee employee) {
        // 社員番号または氏名またはパスワードが空欄だった場合
        if(employee.getAuthentication().getCode().equals("") ||
                employee.getName().equals("") ||
                employee.getAuthentication().getPassword().equals("")) {
            // 従業員登録画面（employee/register.html）に遷移
            return "employee/register";
        }
        LocalDateTime currentTime = LocalDateTime.now(); // 現在日時
        employee.setCreatedAt(currentTime); //　登録日時
        employee.setDeleteFlag(0); //　削除フラグ
        employee.setUpdatedAt(currentTime); //　更新日時
        Authentication a = employee.getAuthentication();
        a.setEmployee(employee);
        String password = a.getPassword();
        a.setPassword(passwordEncoder.encode(password)); // ハッシュ化(パスワードを無意味な文字列に変換)したパスワードをセット
        try {
            // 従業員情報登録
            service.saveEmployee(employee);
        } catch (Exception e) { //エラーが起こった場合登録画面にリダイレクト　※Exception（全エラーを受け取る）
            a.setPassword(""); //問題が起こったときはパスワードを空にして登録画面に戻る
            return "employee/register";
        }
        // 一覧画面（employee/list.html）にリダイレクト
        return "redirect:/employee/list";
    }

    /** 従業員情報編集画面を表示 */
    @GetMapping(value = {"/edit","/edit/{id}/" })
    public String getEmployeeedit(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // idが指定されていたら検索結果、無ければ空のクラスを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // 従業員詳細画面（employee/edit.html）に遷移
        return "employee/edit";
    }

    /** 従業員情報の編集処理(更新) */
    @PostMapping("/edit/{id}/")
    public String postEmployee(@PathVariable(name = "id", required = false) Integer id, Employee employee) {
        Employee tableEmployee = id != null ? service.getEmployee(id) : new Employee();
        if(!employee.getAuthentication().getPassword().equals("")) { //パスワードの欄が空でない場合
            tableEmployee.getAuthentication().setPassword(passwordEncoder.encode(employee.getAuthentication().getPassword())); // パスワード上書き
            // ハッシュ化(パスワードを無意味な文字列に変換)したパスワードをセット
        }
        tableEmployee.setName(employee.getName()); //　氏名上書き
        tableEmployee.getAuthentication().setRole(employee.getAuthentication().getRole()); //　権限上書き
        LocalDateTime currentTime = LocalDateTime.now();
        tableEmployee.setUpdatedAt(currentTime); // 更新日時
        // 従業員情報登録
        service.saveEmployee(tableEmployee);
        // 一覧画面（employee/list.html）にリダイレクト
        return "redirect:/employee/list";
    }

    /** 従業員情報の削除処理 */
    @GetMapping(value = {"/delete","/delete/{id}/" })
    public String getEmployeedelete(@PathVariable(name = "id", required = false) Integer id, Model model) {
        service.removeEmployee(id);
        // 一覧画面（employee/list.html）にリダイレクト
        return "redirect:/employee/list";
    }
}