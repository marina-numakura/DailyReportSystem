package com.techacademy.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import com.techacademy.entity.Employee;
import com.techacademy.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository repository) {
        this.employeeRepository = repository;
    }

    /** 全件を検索して返す */
    public List<Employee> getEmployeeList() {
        // リポジトリのfindAllメソッドを呼び出す
        return employeeRepository.findAll();
    }

    /** 詳細を表示（1件を検索して返す） */
    public Employee getEmployee(Integer id) {
        // findByIdで検索
        Optional<Employee> option = employeeRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Employee employee = option.orElse(null);
        return employee;
    }

    /** 従業員情報の新規登録 */
    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /** 従業員情報を1件検索して返す */
    public Employee getEmployeeEdit(Integer id) {
        // findByIdで検索
        Optional<Employee> option = employeeRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Employee employee = option.orElse(null);
        return employee;
    }

    /** 従業員情報の削除を行なう */
    public Employee removeEmployee(Integer id) {
        Employee currentEmployee = this.getEmployee(id);
        currentEmployee.setDeleteFlag(1);
        return employeeRepository.save(currentEmployee);
    }
}
