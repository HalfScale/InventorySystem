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
            <span id="add-user-item-span" class="item-span">Add user</span>
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
        
        <div id="user-form-div" class="standard-modal">
            <form id="user-form">
                <div class="form-header">
                    <span class="close user-modal-close"> ×</span>
                </div>
                <div id="user-form-content">
<!--                    <input type="text" name="user[username]" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" placeholder="Username">
                    <input type="password" name="user[password]" placeholder="Password">-->
                    <input id="user-username" type="text" name="user" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" placeholder="Username" required>
                    <input id="user-name" type="text" name="user" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" placeholder="Name" required>
                    <input id="user-password" type="password" name="pass" placeholder="Password" autocomplete="new-password" required>
                    <input id="user-re-password" type="password" name="pass" placeholder="Retype Password" autocomplete="new-password" required>
                    <select id="user-select-role" name="" id="" class="select-role" required></select>
                    <input type="submit" value="Save">
                </div>
            </form>
        </div>
        
        <div id="response-dialog" class="standard-modal">
            <div class="response-dialog-content">
                <div class="standard-header">
                </div>

                <div>
                    <section class="standard-section">
                        <span class="response-dialog-text"></span>
                    </section>

                    <section class="standard-section">
                        <button class="response-dialog-confirm-bttn">Confirm</button>
                    </section>
                </div>
            </div>
        </div>
        
        
        <script src="assets/js/user.js"></script>
    </body>
</html>
