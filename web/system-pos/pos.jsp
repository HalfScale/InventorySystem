<%-- 
    Document   : pos
    Created on : 03 4, 19, 8:43:43 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../system_nav.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="assets/css/pos.main.css">
        <link type="text/css" rel="stylesheet" href="../css/util.css">
        <title>POS</title>
    </head>
    <body>
        <div id="view-content">
            <!--table here-->
            <div id="pos-table-div">
                <table>
                    <thead>
                        <th><span>Name</span></th>
                        <th><span>Code</span></th>
                        <th><span>Description</span></th>
                        <th><span>Price</span></th>
                        <th><span>Reseller Price</span></th>
                        <th><span>Stock</span></th>
                    </thead>

                    <tbody class="pos-table-body">
                    </tbody>
                </table>
            </div>

            <!--items to be purchased here-->
            <div id="cart-div">
                <div class="cart-header">
                    <span>Cart</span>
                </div>
                
                <div id="cart-item-list">
                    <!--<div class="cart-item-box">-->
<!--                        <section>
                            <span>Choi Nori Belt Japan</span>
                            <span>CNBJ</span>
                        </section>
                        
                        <section>
                            <span>Php 1000</span>
                            <span>Quantity: 10</span>
                        </section>
                        
                        <section>
                            <span>Total</span>
                            <span> &times; </span>
                        </section>
                    </div>-->
                </div>
                
                <div class="cart-buttons">
                    <button id="checkout-button">Checkout -> &#8369;0.00</button>
                </div>
            </div>

            <!--some footer here-->
            <div>
            </div>
        </div>
        
        <!--Modals-->
        <div id="pos-quantity-modal">
            <div class="pos-modal-content">
                
                <div class="pos-modal-header">
                </div>
                
                <div class="pos-modal-body">
                    <section>
                        <span>Set quantity:</span>
                        <input class="pos-modal-setting-input" type="text" name="item-qty" />
                    </section>
                    
                    <section>
                        <button class="pos-modal-ok-button">Ok</button>
                        <button class="pos-modal-cancel-button">Cancel</button>
                    </section>
                </div>
                
            </div>
        </div>
        
        <script src="assets/js/pos.item-table.js"></script>
        <script src="assets/js/pos.item-management.js"></script>
    </body>
</html>