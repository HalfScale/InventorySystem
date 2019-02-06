<%-- 
    Document   : home
    Created on : 01 23, 19, 10:10:07 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/style-home.css">
        <title>JSP Page</title>
    </head>
    
    <c:url var="logoutURL" value="/SystemController">
        <c:param name="command" value="LOGOUT" />
    </c:url>
    
    <body>
        <div id="test-element" data-itemId="">
        <div class="side-nav">
            <a href="add-item-page.jsp">Add item</a>
            <a href="${logoutURL}">Logout</a>
            <a href="#"></a>
        </div>
        
        <div class="top-nav">
            <span class="add-item-span">Add Item</span>
        </div>
        
        <div id="view-content">
            <table id="item-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Stock</th>
                    </tr>
                </thead>
                
                <tbody class="table-body">
                </tbody>
                
            </table>
            
            <div id="form-modal-container">
                <form action="SystemController" method="GET" id="add-item-form">
                    <input type="hidden" id="command" name="command" value="ADD">
                    
                    <div class="form-header">
                        <span class="close"> &times;</span>
                    </div>

                    <div id="content">
                        <label>Name:</label>
                        <input id="name" type="text" name="name"/>
                        <label>Code:</label>
                        <input id="code" type="text" name="code"/>
                        <label>Description:</label>
                        <input id="description" type="text" name="description"/>
                        <label>Price:</label>
                        <input id="price" type="text" name="price"/>
                        <label>Stock:</label>
                        <input id="stock" type="text" name="stock"/>
                        <input type="submit" value="Add" class="sign-in-button">
                    </div>

                </form>
            </div>
        </div>
     
    <script src="script/inventory.js"></script>
    </body>
</html>
