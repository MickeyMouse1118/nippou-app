<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.nippou.model.Report" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>日報登録・編集</title>
</head>
<body>

<%
    String mode = (String) request.getAttribute("mode"); // "new" または "edit"
    Report report = (Report) request.getAttribute("report"); // editのときのみ値が入る
	String errorMessage = (String) request.getAttribute("errorMessage");

    String actionUrl;
    if ("edit".equals(mode)) {
        actionUrl = request.getContextPath() + "/reports/update";
    } else {
        actionUrl = request.getContextPath() + "/reports/create";
    }
%>

<h1><%= "edit".equals(mode) ? "日報編集" : "日報新規登録" %></h1>

<% if (errorMessage != null) { %>
	<p style="color:red;"><%= errorMessage %></p>
<% } %>

<form method="post" action="<%= actionUrl %>">

<% if ("edit".equals(mode)) { %>
    <input type="hidden" name="id" value="<%= report.getId() %>">
<% } %>

    <p>
        報告者名:<br>
        <input type="text" name="reporterName" value="<%= (report != null && report.getReporterName() != null) ? report.getReporterName() : "" %>">
    </p>

    <p>
        対象日:<br>
        <input type="date" name="reportDate" value="<%= (report != null && report.getReportDate() != null) ? report.getReportDate() : "" %>">
    </p>

    <p>
        作業内容:<br>
        <textarea name="workContent" rows="5" cols="50"><%= (report != null && report.getWorkContent() != null) ? report.getWorkContent() : "" %></textarea>
    </p>

    <p>
        所感:<br>
        <textarea name="remarks" rows="3" cols="50">${report.remarks}</textarea>
    </p>

    <p>
        <input type="submit" value="<%= "edit".equals(mode) ? "更新" : "登録" %>">
    </p>

</form>

<p><a href="<%= request.getContextPath() %>/reports">一覧へ戻る</a></p>

</body>
</html>
