<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../system_nav.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    
    <c:url var="logoutURL" value="/SystemController">
        <c:param name="command" value="LOGOUT" />
    </c:url>
    
    <body>
        
    </body>
</html>
