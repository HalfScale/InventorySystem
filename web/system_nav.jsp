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
        <link type="text/css" rel="stylesheet" href="../assets/css/side-nav.css">
        <link type="text/css" rel="stylesheet" href="../assets/css/icons.css">
        <link type="text/css" rel="stylesheet" href="../assets/css/side-nav.css">
        <link type="text/css" rel="stylesheet" href="../assets/css/util.css">
        <link type="text/css" rel="stylesheet" href="../assets/css/general.css">
        <script src="../assets/js/util.js"></script>
    </head>
    
    <c:url var="logoutURL" value="/SystemController">
        <c:param name="command" value="LOGOUT"/>
    </c:url>
        
    
    <body>
        <div>
        <div class="side-nav">
            <a href="${pageContext.request.contextPath}/system-home/home.jsp"> <div id="home-nav-icon"></div> Home</a>
            <a href="${pageContext.request.contextPath}/system-pos/pos.jsp"> <div id="pos-nav-icon"></div> POS</a>
            <a href="${pageContext.request.contextPath}/system-inventory/inventory.jsp"> <div id="inventory-nav-icon"></div> Inventory</a>
            <a href="${pageContext.request.contextPath}/system-user/user.jsp"> <div id="user-nav-icon"></div> Users</a>
            <a href="${pageContext.request.contextPath}/system/system-log.jsp"> <div id="log-nav-icon"></div>  Logs</a>
            <a href="${logoutURL}"> <div id="logout-nav-icon"></div> Logout</a>
        </div>
    </body>
</html>
