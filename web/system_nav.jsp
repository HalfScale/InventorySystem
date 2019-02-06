<%-- 
    Document   : system_nav
    Created on : 02 6, 19, 9:13:14 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link type="text/css" rel="stylesheet" href="css/style-home.css">
    </head>
    <body>
        <div id="test-element" data-itemId="">
        <div class="side-nav">
            <a href="#">Home</a>
            <a href="item-list.jsp">Inventory</a>
            <a href="${logoutURL}">Logout</a>
        </div>
    </body>
</html>
