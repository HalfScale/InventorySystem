<%-- 
    Document   : user
    Created on : 06 2, 19, 1:48:17 PM
    Author     : marwin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../system_nav.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="assets/css/style.css">
        <title>User</title>
    </head>
    <body>
        <div class="top-nav">
            <span id="role-item-span" class="item-span">Roles</span>
        </div>
        
        <input id="item-search-bar" type="text" placeholder="Search for item...">
        
        <div id="view-content">
            <table id="user-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>UserName</th>
                        <th>Role</th>
                    </tr>
                </thead>
                
                <tbody></tbody>
            </table>
        </div>
        
        <div id="role-modal" class="standard-modal">
            <div class="role-div">
                <div class="form-header">
                    <span class="close role-modal-close"> ×</span>
                </div>
                <form id="role-form">
                    <input class="transaction-item-input" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" type="text" placeholder="Type name of role here...">
                    <input type="submit" value="Add Role">
                </form>
                <div class="scrollable-table">
                    <table id="role-table">
                        <thead>
                            <tr>
                                <th>User Roles</th>
                            </tr>
                        </thead>
                        
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
        
        
        
        <script src="assets/js/user.js"></script>
    </body>
</html>
