<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <style type="text/css">

          @import"../style.css"

    </style>
</head>
<body>
    <h2>Register</h2>
    <form action="../register/table" method="post">
        Username: <input type="text" name="username"><br>
        Password: <input type="password" name="password"><br>
        <input type="submit" value="Register">
    </form>
    <br>
        <a href="../login/menu.jsp">Already have an account? Login here</a>
</body>
</html>