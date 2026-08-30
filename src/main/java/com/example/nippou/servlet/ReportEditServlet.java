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
 * 日報編集フォーム表示。
 * GET /reports/edit?id=xx
 */
@WebServlet("/reports/edit")
public class ReportEditServlet extends HttpServlet {

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
            throw new ServletException("日報の取得に失敗しました", e);
        }

        // mode=edit をJSPに伝え、編集フォームとして表示させる(form.jsp は new/edit 共通)
        request.setAttribute("mode", "edit");
        request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
    }
}
