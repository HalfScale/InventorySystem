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
        <link type="text/css" rel="stylesheet" href="assets/css/main.css">
        <title>POS</title>
    </head>
    <body>
        <!--<div id="view-content">-->
            <!--table here-->
                <input id="item-search-bar" type="text" placeholder="Search for item...">
                <div class="pos-header flex-horizontal">
                    <span class="pos-transaction-bttn clicky">Transactions</span>
                </div>
                
            <div id="pos-table-div">
                
                
                <div class="scrollable">
                    <table id="pos-table">
                        <thead>
                            <th><span>Name</span></th>
                            <th><span>Code</span></th>
                            <th><span>Brand</span></th>
                            <th><span>Category</span></th>
                            <th><span>Description</span></th>
                            <th><span>Price</span></th>
                            <th><span>Reseller Price</span></th>
                            <th><span>Stock</span></th>
                        </thead>

                        <tbody class="pos-table-body">
                        </tbody>
                    </table>
                </div>
            </div>

            <!--items to be purchased here-->
            <div class="cart-header">
                <span>Cart</span>
            </div>
            <div id="cart-div">
                
                <div id="cart-item-list">
                </div>
            </div>
            <div id="checkout-button" class="cart-buttons clicky">
                <!--<button id="checkout-button">Checkout -> &#8369;0.00</button>-->
                <label class="clicky">Checkout (&#8369;0.00)</label>
            </div>

            <div>
            </div>
        <!--</div>-->
        
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
                        <span>Price Type:</span>
                        <select class="pos-modal-price-type">
                            <option value="original">Original Price</option>
                            <option value="reseller">Reseller Price</option>
                        </select>
                    </section>
                    
                    <section>
                        <button class="pos-modal-ok-button">Confirm</button>
                        <button class="pos-modal-cancel-button">Cancel</button>
                    </section>
                </div>
                
            </div>
        </div>
        
        <div id="checkout-modal">
            
            <div class="checkout-modal-content">
                
                <div class="pos-modal-header">
                </div>
                
                <div class="checkout-modal-body">
                    <section>
                        <span>TOTAL OVERVIEW</span>
                    </section>
                    
                    <section>
                        <h3 class="checkout-total"></h3>
                    </section>
                    
                    <section>
                        <span>Type of transaction:</span>
                        <select class="transaction-type-select">
                        </select>
                    </section>
                    
                    <section>
                        <button class="checkout-confirm">CHECKOUT</button>
                        <button class="checkout-cancel">CANCEL</button>
                    </section>
                </div>
                
            </div>
        </div>
        
        <div id="pos-transaction-modal" class="standard-modal">
            <div class="standard-table-div">

                <div class="standard-div-header flex-horizontal justify-space-between">
                    <span class="white-text-bold">TRANSACTIONS</span>
                    <span class="close-bttn transaction-modal-close"> &times;</span>
                </div>

                <div class="scrollable-table">
                    <table id="transaction-table">
                        <thead>
                            <tr>
                                <th>Transaction Type</th>
                                <th>Total Amount</th>
                                <th>Total Quantity</th>
                                <th>Timestamp</th>
                            </tr>
                        </thead>

                        <tbody class="white-bg text-center">
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
        
        <div id="pos-transaction-detail-modal" class="standard-modal">
            <div class="standard-table-div">

                <div class="standard-div-header flex-horizontal justify-space-between">
                    <span class="white-text-bold">TRANSACTION DETAIL</span>
                    <span class="close-bttn transaction-detail-modal-close"> &times;</span>
                </div>

                <div class="scrollable-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Item Name</th>
                                <th>Brand</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Reseller Price</th>
                                <th>Quantity</th>
                                <th>Total Amount</th>
                            </tr>
                        </thead>

                        <tbody class="white-bg text-center">
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
        
        <div id="response-dialog" class="standard-modal">
            <div class="response-dialog-content">
                <div class="standard-div-header">
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
        
        <script src="assets/js/item-table.js"></script>
        <script src="assets/js/item-management.js"></script>
        <script src="assets/js/transaction.js"></script>
    </body>
</html>
