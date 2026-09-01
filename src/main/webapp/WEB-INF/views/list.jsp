<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.nippou.model.Report" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>日報一覧</title>
</head>
<body>

<h1>ミッキーマウス</h1>

<button style="font-size:18px; border-radius: 8px; color:red;"><a href="<%= request.getContextPath() %>/reports/new" style="color:black;">SHNKITOROKU</a></button>

<p style="font-size:20px;">全<%= request.getAttribute("reportCount") %>件</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>報告者</th>
        <th>対象日</th>
        <th>作業内容</th>
        <th>詳細</th>
    </tr>
<%
    List<Report> reportList = (List<Report>) request.getAttribute("reportList");
    if (reportList != null) {
        for (Report report : reportList) {
%>
    <tr>
        <td><%= report.getId() %></td>
        <td><%= report.getReporterName() %></td>
        <td><%= report.getReportDate().replace("-", "/") %></td>
        <td><%= report.getWorkContent() %></td>
        <td><a href="<%= request.getContextPath() %>/reports/detail?id=<%= report.getId() %>">詳細</a></td>
    </tr>
<%
        }
    }
%>
</table>

</body>
</html>
