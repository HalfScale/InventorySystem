<%-- 
    Document   : add-item-page
    Created on : 01 27, 19, 11:26:03 AM
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
        <div class="form-container">
            <form action="SystemController" method="GET" id="login-form">
                <input type="hidden" name="command" value="ADD">
                <div class="form-header">
                    <h2 class="login-header">Add Item</h2>
                </div>
                
                <div id="content">
                    <label>Name:</label>
                    <input type="text" name="name"/>
                    <label>Code:</label>
                    <input type="text" name="code"/>
                    <label>Description:</label>
                    <input type="text" name="description"/>
                    <label>Price:</label>
                    <input type="text" name="price"/>
                    <label>Stock:</label>
                    <input type="text" name="stock"/>
                    <input type="submit" value="Add" class="sign-in-button">
                </div>
                
                <a href="SystemController">Back to home</a>
            </form>
        </div>
    </body>
</html>
