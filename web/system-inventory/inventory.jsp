<%-- 
    Document   : home
    Created on : 01 23, 19, 10:10:07 PM
    Author     : Muffin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../system_nav.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/style.css">
        <title>Inventory</title>
    </head>
    
    <c:url var="logoutURL" value="/SystemController">
        <c:param name="command" value="LOGOUT" />
    </c:url>
    
    <body>
        
        <div class="top-nav">
            <span id="archive-item-span" class="item-span">Archive</span>
            <span id="add-item-span" class="item-span">Add Item</span>
            <span id="categories-item-span" class="item-span">Category</span>
            <span id="brand-item-span" class="item-span">Brand</span>
            <span id="transaction-item-span" class="item-span">Transaction Type</span>
        </div>
        
        <input id="item-search-bar" type="text" placeholder="Search for item...">
        <div id="view-content">
            <div class="main-table-div">
                <table id="item-table">
                    <thead>
                        <tr>
                            <th><span>Name</span></th>
                            <th><span>Code</span></th>
                            <th><span>Brand</span></th>
                            <th><span>Category</span></th>
                            <th><span>Description</span></th>
                            <th><span>Price</span></th>
                            <th><span>Reseller Price</span></th>
                            <th><span>Stock</span></th>
                        </tr>
                    </thead>

                    <div>
                        <tbody class="table-body">
                        </tbody>
                    </div>

                </table>
            </div>
            
            <!-- MODALS -->
            <div class="form-modal-container add-item-modal">
                <form action="SystemController" method="GET" id="add-item-form">
                    <input type="hidden" id="command" name="command" value="ADD">
                    
                    <div class="form-header">
                        <span class="close add-modal-close"> &times;</span>
                    </div>

                    <div id="add-form-content">
                        <label>Name:</label>
                        <input id="add-name" type="text" name="name" required/>
                        <label>Code:</label>
                        <input id="add-code" type="text" name="code"/>
                        <label>Description:</label>
                        <input id="add-description" type="text" name="description" required/>
                        <label>Brand:</label>
                        <select id="add-brand" class="select-generic" name="brand" required=""></select>
                        <label>Category:</label>
                        <select id="add-category" class="select-generic" name="description" required=""></select>
                        <label>Price:</label>
                        <input id="add-price" type="text" name="price" pattern="^\d+(\.\d{1,2})?$" title="Up to two decimal places" required/>
                        <label>Reseller Price:</label>
                        <input id="add-reseller-price" type="text" pattern="^\d+(\.\d{1,2})?$" title="Up to two decimal places" name="reseller-price" required/>
                        <label>Stock:</label>
                        <input id="add-stock" type="text" name="stock" pattern="^\d+$" title="Decimal places and letter are not allowed" required/>
                        <input type="submit" value="Add" class="sign-in-button">
                    </div>

                </form>
            </div>
            
            <div class="form-modal-container update-item-modal">
                <form action="SystemController" method="GET" id="update-item-form">
                    
                    <div class="form-header">
                        <span class="close update-modal-close"> &times;</span>
                    </div>
                    
                    <div id="update-form-content">
                        <input id="update-action" type="hidden">
                        <input id="update-id" type="hidden" name="id">
                        <label>Name:</label>
                        <input id="update-name" type="text" name="name" required/>
                        <label>Code:</label>
                        <input id="update-code" type="text" name="code" required/>
                        <label>Brand:</label>
                        <select id="update-brand" class="select-generic" name="brand" required=""></select>
                        <label>Category:</label>
                        <select id="update-category" class="select-generic" name="description" required=""></select>
                        <label>Description:</label>
                        <input id="update-description" type="text" name="description" required/>
                        <label>Price:</label>
                        <input id="update-price" type="text" name="price" required/>
                        <label>Reseller Price:</label>
                        <input id="update-reseller-price" type="text" name="price" required/>
                        <label>Stock:</label>
                        <input id="update-stock" type="text" name="stock" required/>
                        
                        <input type="submit" value="Update">
                        <input type="button" value="History" class="history-item-button">
                        <input type="button" value="Delete" class="delete-item-button">
                    </div>
                   

                </form>
            </div>
            
            <div class="form-modal-container history-item-modal">
                <div class="item-history-div">
                    
                    <div class="form-header">
                        <span class="close history-modal-close"> &times;</span>
                    </div>
                    
                    <div class="scrollable-table">
                        <table>
                            <thead>
                                <tr>
                                    <th colspan="8">ITEM HISTORY</th>
                                </tr>
                                <tr>
                                    <th>Name</th>
                                    <th>Code</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Reseller Price</th>
                                    <th>Stock</th>
                                    <th>Action</th>
                                    <th>Timestamp</th>
                                </tr>
                            </thead>

                            <tbody class="item-history-tbody">
                            </tbody>
                        </table>
                    </div>
                    
                </div>
            </div>
            <div class="form-modal-container history-item-modal">
                <div class="item-history-div">
                    
                    <div class="form-header">
                        <span class="close history-modal-close"> &times;</span>
                    </div>
                    
                    <div class="scrollable-table">
                        <table>
                            <thead>
                                <tr>
                                    <th colspan="8">ITEM HISTORY</th>
                                </tr>
                                <tr>
                                    <th>Name</th>
                                    <th>Code</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Reseller Price</th>
                                    <th>Stock</th>
                                    <th>Action</th>
                                    <th>Timestamp</th>
                                </tr>
                            </thead>

                            <tbody class="item-history-tbody">
                            </tbody>
                        </table>
                    </div>
                    
                </div>
            </div>
            
            <div class="form-modal-container archive-item-modal">
                <div class="item-archive-div">
                    
                    <div class="form-header">
                        <span class="close archive-modal-close"> &times;</span>
                    </div>
                    
                    <div class="scrollable-table">
                        <table>
                            <thead>
                                <tr>
                                    <th colspan="8">ITEM ARCHIVE</th>
                                </tr>
                                <tr>
                                    <th>Name</th>
                                    <th>Code</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Reseller Price</th>
                                    <th>Stock</th>
                                    <th>Timestamp</th>
                                </tr>
                            </thead>

                            <tbody class="item-archive-tbody">
                            </tbody>
                        </table>
                    </div>
                    
                </div>
            </div>
            
            <div class="form-modal-container brand-item-modal">
                <div class="item-brand-div">
                    
                    <div class="form-header">
                        <span class="close brand-modal-close"> &times;</span>
                    </div>
                    
                    <form id="brand-form">
                        <input class="brand-item-input" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" type="text" placeholder="Type the brand here...">
                        <input type="submit" value="Add Brand">
                    </form>
                    
                    <div class="scrollable-table">
                        <table id="brand-table">
                            <thead>
                                <tr>
                                    <th>Brand</th>
                                </tr>
                            </thead>

                            <tbody class="item-brand-tbody">
                            </tbody>
                        </table>
                    </div>
                    
                </div>
            </div>
            
            <div class="form-modal-container category-item-modal">
                <div class="item-category-div">

                    <div class="form-header">
                        <span class="close category-modal-close"> &times;</span>
                    </div>
                    
                    <form id="category-form">
                        <input class="category-item-input" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" type="text" placeholder="Type the brand here...">
                        <input type="submit" value="Add Category">
                    </form>
                    
                    <div class="scrollable-table">
                        <table id="category-table">
                            <thead>
                                <tr>
                                    <th colspan="1">Category</th>
                                </tr>
                            </thead>

                            <tbody class="item-category-tbody">
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
            
            <div class="form-modal-container transaction-item-modal">
                <div class="item-transaction-div">

                    <div class="form-header">
                        <span class="close transaction-modal-close"> &times;</span>
                    </div>
                    
                    <form id="transaction-form">
                        <input class="category-item-input" pattern="^[^-\s][a-zA-Z0-9_\s-]+$" type="text" placeholder="Type the brand here...">
                        <input type="submit" value="Add Category">
                    </form>
                    
                    <div class="scrollable-table">
                        <table id="transaction-table">
                            <thead>
                                <tr>
                                    <th colspan="1">Transaction Type</th>
                                </tr>
                            </thead>

                            <tbody class="item-transaction-tbody">
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
            
            <div id="brand-delete-modal" class="standard-modal">
                <div class="brand-modal-content">
                    <!--header-->
                    <div class="standard-header">
                    </div>
                    
                    <div class="brand-modal-body">
                        <section class="standard-section">
                            <span class="brand-modal-text standard-span"></span>
                        </section>
                        
                        <section class="standard-section">
                            <button class="brand-confirm-button">Confirm</button>
                            <button class="brand-cancel-button">Cancel</button>
                        </section>
                    </div>
                </div>
            </div>
            
            <div id="category-delete-modal" class="standard-modal">
                <div class="category-modal-content">
                    <!--header-->
                    <div class="standard-header">
                    </div>
                    
                    <div class="category-modal-body">
                        <section class="standard-section">
                            <span class="category-modal-text standard-span"></span>
                        </section>
                        
                        <section class="standard-section">
                            <button class="category-confirm-button">Confirm</button>
                            <button class="category-cancel-button">Cancel</button>
                        </section>
                    </div>
                </div>
            </div>
        </div>
     
    <script src="js/inventory.js"></script>
    </body>
</html>
