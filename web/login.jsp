<%-- 
    Document   : login
    Created on : 01 21, 19, 8:55:21 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/style.css">
        <title>Login</title>
    </head>
    <body>
        <div class="form-container">
            <form action="SystemLogin" method="POST" id="login-form">
                <div class="form-header">
                    <h2 class="login-header">Login</h2>
                </div>
                
                <div id="content">
                    <label>Username:</label>
                    <input type="text" name="username"/>
                    <label>Password:</label>
                    <input type="password" name="password"/>
                    <input type="submit" value="Sign in" class="sign-in-button">
                </div>
            </form>
        </div>
    </body>
</html>
