package com.example.nippou.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.nippou.dao.ReportDao;
import com.example.nippou.model.Report;

/**
 * 日報更新処理。
 * POST /reports/update
 */
@WebServlet("/reports/update")
public class ReportUpdateServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String reporterName = request.getParameter("reporterName");
        String reportDate = request.getParameter("reportDate");
        String workContent = request.getParameter("workContent");
        String remarks = request.getParameter("remarks");
        
        Report report = new Report();
        report.setId(id);
        report.setReporterName(reporterName);
        report.setReportDate(reportDate);
        report.setWorkContent(workContent);
        report.setRemarks(remarks);
        
        if (reportDate == null || reportDate.trim().isEmpty()) {
            request.setAttribute("mode", "new");
            request.setAttribute("errorMessage", "対象日を入力してください。");
            request.setAttribute("report", report);
            request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
            return;
        }
        
        LocalDate selectedDate = LocalDate.parse(reportDate);
        LocalDate today = LocalDate.now();
        if (selectedDate.isAfter(today)) {
            request.setAttribute("mode", "new");
            request.setAttribute("errorMessage", "対象日に未来の日付を指定することはできません。");
            request.setAttribute("report", report); 
            request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
            return;
        }
         
        if (workContent == null || workContent.trim().isEmpty()) {
            request.setAttribute("mode", "new");
            request.setAttribute("errorMessage" , "作業内容を入力してください");
            request.setAttribute("report", report);
            request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
            return;
        }

        try {
            reportDao.update(report);
        } catch (SQLException e) {
            throw new ServletException("日報の更新に失敗しました", e);
        }

        response.sendRedirect(request.getContextPath() + "/reports/detail?id=" + id);
    }
}
