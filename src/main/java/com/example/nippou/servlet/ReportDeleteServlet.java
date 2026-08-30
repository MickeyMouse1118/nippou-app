package com.example.nippou.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.nippou.dao.ReportDao;

/**
 * 日報削除処理。
 * POST /reports/delete
 */
@WebServlet("/reports/delete")
public class ReportDeleteServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            reportDao.delete(id);
        } catch (SQLException e) {
            throw new ServletException("日報の削除に失敗しました", e);
        }

        response.sendRedirect(request.getContextPath() + "/reports");
    }
}