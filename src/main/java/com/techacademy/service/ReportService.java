package com.techacademy.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService  {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository repository) {
        this.reportRepository = repository;
    }

    /** 全件を検索して返す */
    public List<Report> getReportList() {
        // リポジトリのfindAllメソッドを呼び出す
        return reportRepository.findAll();
    }

    /** 詳細を表示（1件を検索して返す） */
    public Report getReport(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    /** 従業員情報の新規登録 */
    @Transactional
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    /** 従業員情報を1件検索して返す */
    public Report getReportEdit(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    /**　追記　*/
    public List<Report> getfindByEmployee() {
        return reportRepository.findAll();
    }

    /** ログインしている従業員の日報一覧を表示 */
    public List<Report> getReportList(Employee employee) {
        // findByIdで検索
        List<Report> reports = reportRepository.findByEmployee(employee);
        // 取得できなかった場合はnullを返す
        return reports;
    }
}
