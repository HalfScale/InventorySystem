<%-- 
    Document   : reports
    Created on : 06 15, 19, 9:13:23 AM
    Author     : marwin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../system_nav.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="assets/css/main.css">
        <link type="text/css" rel="stylesheet" href="assets/css/icons.css">
        <title>Reports</title>
    </head>
    <body>
        <div class="view-content">
            
            <div id="date-picker-dialog" class="hidden-elem" name="transaction_date">
                <span>Date:</span><input type="text" id="datepicker">
            </div>
            
            <div id="month-picker-dialog" class="hidden-elem">
                <span>Date:</span><input type="text" id="monthpicker">
            </div>
            
            <div id="year-picker-dialog" class="hidden-elem">
                <span>Date:</span><input type="text" id="yearpicker">
            </div>
            
        </div>
        
        <script src="assets/js/reports.js"></script>
        <script src="assets/js/test.js"></script>
    </body>
</html>
