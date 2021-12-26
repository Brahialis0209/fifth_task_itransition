<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Log in with your account</title>
  <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
</head>

<body>
<div>
  <table>
    <thead>
    <th>ID</th>
    <th>UserName</th>
    <th>Mail</th>
    <th>Registration date</th>
    <th>Log date</th>
    <th>Status</th>
    </thead>
    <c:forEach items="${allUsers}" var="user">
      <tr>
        <td>${user.id}</td>
        <td>${user.username}</td>
        <td>${user.mail}</td>
        <td>${user.regdate}</td>
        <td>${user.logdate}</td>
        <td>${user.status}</td>
        <td>
          <form action="${pageContext.request.contextPath}/usr" method="post">
            <input type="hidden" name="userId" value="${user.id}"/>
            <input type="hidden" name="action" value="delete"/>
            <button type="submit">Delete</button>
          </form>
        </td>
        <td>
          <form action="${pageContext.request.contextPath}/usr" method="post">
            <input type="hidden" name="userId" value="${user.id}"/>
            <input type="hidden" name="action" value="block"/>
            <button type="submit">Block</button>
          </form>
        </td>
        <td>
          <form action="${pageContext.request.contextPath}/usr" method="post">
            <input type="hidden" name="userId" value="${user.id}"/>
            <input type="hidden" name="action" value="unblock"/>
            <button type="submit">Unblock</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
  <a href="/">Главная</a>
</div>
</body>
</html>
