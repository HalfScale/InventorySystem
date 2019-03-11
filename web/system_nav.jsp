<%-- 
    Document   : system_nav
    Created on : 02 6, 19, 9:13:14 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link type="text/css" rel="stylesheet" href="../css/general.css">
    </head>
    <body>
        <%--<c:url value="/SystemController" var="logoutURL">--%>
            <%--<c:param name="command" value="LOGOUT" />--%>
        <%--</c:url>--%>
        <div id="test-element" data-itemId="">
        <div class="side-nav">
            <a href="${pageContext.request.contextPath}/system-home/home.jsp">Home</a>
            <a href="${pageContext.request.contextPath}/system-pos/pos.jsp">POS</a>
            <a href="${pageContext.request.contextPath}/system-inventory/inventory.jsp">Inventory</a>
            <a href="${logoutURL}">Logout</a>
        </div>
    </body>
</html>
