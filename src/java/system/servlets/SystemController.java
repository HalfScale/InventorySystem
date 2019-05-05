/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import system.util.Console;
import system.util.GsonUTCDateAdapter;
import system.util.DbUtil;
import system.valueobject.Brand;
import system.valueobject.Category;
import system.valueobject.Item;
import system.valueobject.ItemArchive;
import system.valueobject.ItemLog;
import system.valueobject.LogType;
import system.valueobject.SystemLog;
import system.valueobject.TransactionType;
import system.valueobject.User;

/**
 *
 * @author Muffin
 */
@WebServlet(name = "SystemController", urlPatterns = {"/SystemController"})
public class SystemController extends HttpServlet {
    
    @Resource(name="jdbc/inventory_sys")
    private DataSource datasource;
    DbUtil dbUtil;
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String command = request.getParameter("command");
        Console.log("command", command);

        command = command != null ? command : "LIST";
        System.out.println("Param doGet " + command);

        try {
            switch (command) {
                case "LIST":
                    listItems(request, response);
                    break;
                case "ADD":
                    addItem(request, response);
                    break;
                case "LOAD":
                    loadItem(request, response);
                    break;
                case "UPDATE":
                    updateItem(request, response);
                    break;
                case "DELETE":
                    deleteItem(request, response);
                    break;
                case "HISTORY":
                    viewItemHistory(request, response);
                    break;
                case "ARCHIVE":
                    viewItemArchive(request, response);
                    break;
                case "LOG_TYPE":
                    listLogTypes(request, response);
                    break;
                case "LOGS":
                    listAllLogs(request, response);
                    break;
                case "ADD_BRAND":
                    addBrand(request, response);
                    break;
                case "DELETE_BRAND":
                    deleteBrand(request, response);
                    break;
                case "ADD_CATEGORY":
                    addCategory(request, response);
                    break;
                case "DELETE_CATEGORY":
                    deleteCategory(request, response);
                    break;
                case "LIST_BRAND":
                    listBrands(request, response);
                    break;
                case "LIST_CATEGORY":
                    listCategories(request, response);
                    break;
                case "LIST_TRANSACTION_TYPE": 
                    listTransactionType(request, response);
                    break;
                case "ADD_TRANSACTION_TYPE": 
                    addTransactionType(request, response);
                    break;
                case "DELETE_TRANSACTION_TYPE": 
                    deleteTransactionType(request, response);
                    break;
                case "CHECKOUT": 
                    checkoutItems(request, response);
                    break;
                case "LOGOUT":
                    logout(request, response);
                    break;
                default:
                    listItems(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // <editor-fold defaultstate="expanded" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    @Override
    public void init() throws ServletException {
        dbUtil = new DbUtil(datasource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void listItems(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
//        try {
              //get all the items the convert it to json string
              Gson gson = new Gson();
              List<Item> items = dbUtil.getAllItems();
              String result = gson.toJson(items);
              
              
              // then send the response back to javascript
              PrintWriter out = response.getWriter();
              response.setHeader("Content-Type", "application/json");
              System.out.println("result" + result);
              out.println(result);
            
//        }catch(Exception e){
//            throw new ServletException(e);
//        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        User user = (User) request.getSession(false).getAttribute("active_user");
        if(user == null) {
            response.sendRedirect(request.getContextPath() + "/");
            System.out.println("Session is null");
        }else {
            System.out.println("User: " + user.getName() + " has logged out!");
            registerSystemLog(request, response, LogType.LOGOUT);
            request.getSession(false).invalidate();
            response.sendRedirect(request.getContextPath() + "/");
        }
        
        
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String itemParam = request.getParameter("param");
        Console.log("itemParam", itemParam);
        
        Gson gson = new Gson();
        Item item = gson.fromJson(itemParam, Item.class);
        
        dbUtil.addItem(item);
        registerSystemLog(request, response, LogType.ADD_ITEM);
    }

    private void loadItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String itemId = request.getParameter("itemId");
        
        Item item = dbUtil.loadItem(Integer.valueOf(itemId));
        
        Gson gson = new Gson();
        String jsonItem = gson.toJson(item, Item.class);
        
        response.setHeader("Content-Type", "application/json");
        PrintWriter out = response.getWriter();
        out.println(jsonItem);
        
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
       
        String param = request.getParameter("param");
        System.out.println("parm: " + param);
        Gson gson = new Gson();
        
        Item item = gson.fromJson(param, Item.class);
        
        dbUtil.updateItem(item);
        
        registerSystemLog(request, response, LogType.UPDATE_ITEM);
        
        PrintWriter out = response.getWriter();
        out.println(item.getId());
//        
        
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String itemId = request.getParameter("itemId");
        
        dbUtil.deleteItem(Integer.valueOf(itemId));
        
        PrintWriter out = response.getWriter();
        out.println(itemId);
        registerSystemLog(request, response, LogType.DELETE_ITEM);
    }

    private void viewItemHistory(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .create();
        
        String itemId = request.getParameter("itemId");
        List<ItemLog> itemLogs = dbUtil.viewItemHistory(Integer.parseInt(itemId));
        String result = gson.toJson(itemLogs);
        
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "application/json");
        out.println(result);
    }

    private void viewItemArchive(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .create();
        
        List<ItemArchive> items = dbUtil.viewItemArchive();
        String result = gson.toJson(items);
        
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "application/json");
        out.println(result);
    }

    private void checkoutItems(HttpServletRequest request, HttpServletResponse response)
        throws Exception{
        
        String itemDetails = request.getParameter("param");
        Console.log("checkout items", itemDetails);
        System.out.println("itemDetails " + itemDetails);
        
        JsonObject checkOutItems = new JsonParser().parse(itemDetails).getAsJsonObject();
        String type = checkOutItems.get("type").getAsString();
        JsonArray items = checkOutItems.get("checkoutItem[]").getAsJsonArray();
        
        dbUtil.checkOutItems(items, type);
//        
//        PrintWriter out = response.getWriter();
//        response.setHeader("Content-Type", "text/plain");
//        out.println("Checkout successful!");
//        registerSystemLog(request, response, LogType.CHECKOUT_ITEM);
    }

    private void listLogTypes(HttpServletRequest request, HttpServletResponse response) 
        throws Exception {
        
        Gson gson = new Gson();
        List<LogType> logTypes = dbUtil.getAllLogTypes();
        
        String result = gson.toJson(logTypes);
        
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "application/json");
        out.println(result);
    }

    private void listAllLogs(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        Gson gson = new GsonBuilder().
                registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .create();
        
        List<Map> systemLogs = dbUtil.getAllLogs();
        
        String result = gson.toJson(systemLogs);
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "application/json");
        out.println(result);
        
    }

    private void registerSystemLog(HttpServletRequest request, HttpServletResponse response, int type) 
        throws Exception {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("active_user");
        
        System.out.println("session != null: " + session != null);
        if(session != null && user != null) {
            dbUtil.registerLog(type, user);
            System.out.println("User: " + user.getName());
        }else {
            System.out.println("session and user is null");
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }

    private void addBrand(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String param = request.getParameter("param");
        System.out.println("addBrand method");
        String message = dbUtil.addBrand(param);
        
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "text/plain");
        out.println(message);
        
        registerSystemLog(request, response, LogType.ADD_BRAND);
    }

    private void listBrands(HttpServletRequest request, HttpServletResponse response) 
        throws Exception {
        
        Gson gson = new Gson();
        
        List<Brand> brands = dbUtil.listBrand();
        String result = gson.toJson(brands);
        PrintWriter out = response.getWriter();
        
        out.println(result);
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        Gson gson = new Gson();
        List<Category> categories = dbUtil.listCategory();
        String result = gson.toJson(categories);
        PrintWriter out = response.getWriter();
        
        out.println(result);
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String param = request.getParameter("param");
        
        String message = dbUtil.addCategory(param);
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "text/plain");
        out.println(message);
        
        registerSystemLog(request, response, LogType.ADD_CATEGORY);
    }

    private void deleteBrand(HttpServletRequest request, HttpServletResponse response) 
        throws Exception {
         
        String id = request.getParameter("id");
        
        dbUtil.deleteBrand(Integer.valueOf(id));
        
        response.setHeader("Content-Type","text/plain");
        PrintWriter out = response.getWriter();
        out.println(id);
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String id = request.getParameter("id");
        
        dbUtil.deleteCategory(Integer.valueOf(id));
        
        response.setHeader("Content-Type", "text/plain");
        PrintWriter out = response.getWriter();
        out.println(id);
    }

    private void listTransactionType(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        Gson gson = new Gson();
        List<TransactionType> transactionTypes = dbUtil.listTransactionType();
        
        String result = gson.toJson(transactionTypes);
        
        PrintWriter out = response.getWriter();
        out.println(result);
        
    }

    private void addTransactionType(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String param = request.getParameter("param");
        Console.log("param", param);
        String message = dbUtil.addTransactionType(param);
        Console.log("message", message);
        
        response.setHeader("Content-Type", "text/plain");
        PrintWriter out = response.getWriter();
        out.println(message);
    }

    private void deleteTransactionType(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String id = request.getParameter("id");
        
        dbUtil.deleteTransactionType(Integer.parseInt(id));
        
        Console.log("Transaction type delete successful!");
        
        response.setHeader("Content-Type", "text/plain");
        PrintWriter out = response.getWriter();
        out.println(id);
    }
    
}