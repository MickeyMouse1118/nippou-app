package com.example.nippou.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 日報新規登録フォーム表示。
 * GET /reports/new
 */
@WebServlet("/reports/new")
public class ReportNewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // mode=new をJSPに伝え、登録フォームとして表示させる(form.jsp は new/edit 共通)
        request.setAttribute("mode", "new");
        request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
    }
}
