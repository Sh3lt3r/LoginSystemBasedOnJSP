<%--
  Created by IntelliJ IDEA.
  User: 小花
  Date: 2023/12/18
  Time: 8:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <div class = "table">
        <h2>Login</h2>
        <form action="../login/menu" method="post">
            Username: <input type="text" name="username"><br>
            Password: <input type="password" name="password"><br>
            <input type="submit" value="Login">
        </form>
        <br>
        <a href="../register/table.jsp">Need a new account? Register here.</a>
    </div>
</body>
</html>