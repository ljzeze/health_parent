<%--
  Created by IntelliJ IDEA.
  User: Eric
  Date: 2021/1/15
  Time: 8:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}
</body>
</html>
