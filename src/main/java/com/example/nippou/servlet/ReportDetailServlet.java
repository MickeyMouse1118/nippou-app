package com.example.nippou.servlet;

import com.example.nippou.dao.ReportDao;
import com.example.nippou.model.Report;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 日報詳細画面。
 * GET /reports/detail?id=xx
 */
@WebServlet("/reports/detail")
public class ReportDetailServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            Report report = reportDao.findById(id);
            if (report == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された日報が見つかりません");
                return;
            }
            request.setAttribute("report", report);
        } catch (SQLException e) {
            throw new ServletException("日報詳細の取得に失敗しました", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/detail.jsp").forward(request, response);
    }
}
