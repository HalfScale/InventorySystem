<%-- 
    Document   : system
    Created on : 03 27, 19, 9:17:16 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../system_nav.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="assets/css/system.main.css">
        <title>System Log</title>
    </head>
    <body>
        <main>
            <div class="log-div">
                <select class="log-type-selection">
                </select>

                <table id="log-table">
                    <thead>
                        <th>User</th>
                        <th>Log Type</th>
                        <th>Timestamp</th>
                    </thead>

                    <tbody class="log-table-body">
                    </tbody>
                </table>
            </div>
        </main>
        
        <script src="assets/js/system.main.js"></script>
    </body>
</html>
