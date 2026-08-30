<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.nippou.model.Report" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>日報詳細</title>
</head>
<body>

<h1>日報詳細</h1>

<%
    Report report = (Report) request.getAttribute("report");
%>

<table border="1" cellpadding="6" cellspacing="0">
    <tr><th>ID</th><td><%= report.getId() %></td></tr>
    <tr><th>報告者</th><td><%= report.getReporterName() %></td></tr>
    <tr><th>対象日</th><td><%= report.getReportDate().replace("-", "/") %></td></tr>
    <tr><th>作業内容</th><td><%= report.getWorkContent() %></td></tr>
    <tr><th>登録日時</th><td><%= report.getCreatedAt().toString().replace(".0", "") %></td></tr>
    <tr><th>更新日時</th><td><%= report.getCreatedAt().toString().replace(".0", "") %></td></tr>
	<tr><th>所感</th><td><%= report.getRemarks() == null ? "なし" : report.getRemarks()%></td></tr>
</table>

<p>
    <a href="<%= request.getContextPath() %>/reports/edit?id=<%= report.getId() %>">編集</a>
    | <a href="<%= request.getContextPath() %>/reports">一覧へ戻る</a>
</p>
<form method="post" action="<%= request.getContextPath() %>/reports/delete"
      onsubmit="return confirm('本当に削除しますか?');">
    <input type="hidden" name="id" value="<%= report.getId() %>">
    <input type="submit" value="削除" style="font-size:18px; color:red;">
</form>

</body>
</html>
