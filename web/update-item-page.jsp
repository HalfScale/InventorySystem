<%-- 
    Document   : update-item-page
    Created on : 01 27, 19, 3:12:11 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/style.css">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="SystemController" method="GET" id="login-form">
                <input type="hidden" name="command" value="UPDATE">
                <input type="hidden" name="itemId" value="${item.id}">
                <div class="form-header">
                    <h2 class="login-header">Update Item</h2>
                </div>
                
                <div id="content">
                    <label>Name:</label>
                    <input type="text" name="name" value="${item.name}"/>
                    <label>Code:</label>
                    <input type="text" name="code" value="${item.code}"/>
                    <label>Description:</label>
                    <input type="text" name="description" value="${item.description}"/>
                    <label>Price:</label>
                    <input type="text" name="price" value="${item.price}"/>
                    <label>Stock:</label>
                    <input type="text" name="stock" value="${item.stock}"/>
                    <input type="submit" value="Update" class="sign-in-button">
                </div>
                
                <a href="SystemController">Back to home</a>
            </form>
        </div>
    </body>
</html>
