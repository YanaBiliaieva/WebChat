<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<body>
<h1>Welcome to login page</h1>
${error}
<form action="${loginHandler}" method="post">
    login:<input typpe="text" name="login" placeholder="enter login here"/>
    <br>
    password: <input typpe="text" name="password" placeholder="enter password here"/>
</br>
    <br> <input type="submit" value="Send"/>
</form>
<a href="${spring:mvcUrl('registrationUser')}">Registration</a>
<a href="${spring:mvcUrl('loginUser')}">Login</a>
</body>
</html>
