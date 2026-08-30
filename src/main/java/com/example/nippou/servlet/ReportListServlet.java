package com.example.nippou.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.nippou.dao.ReportDao;
import com.example.nippou.model.Report;

/**
 * 日報一覧画面。
 * GET /reports
 */
@WebServlet("/reports")
public class ReportListServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Report> reportList = reportDao.findAll();
            request.setAttribute("reportList", reportList);
            request.setAttribute("reportCount", reportList.size());
        } catch (SQLException e) {
            throw new ServletException("日報一覧の取得に失敗しました", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/list.jsp").forward(request, response);
    }
}
