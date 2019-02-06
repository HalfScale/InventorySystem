/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import system.util.DbUtil;
import system.valueobject.Item;

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SystemController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SystemController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String command = request.getParameter("command");
        
        if (command == null) {
            command = "LIST";
        }
        
        try {
            switch (command) {
                case "LIST": listItems(request, response);
                    break;
                case "ADD": addItem(request, response);
                    break;
                case "LOAD": loadItem(request, response);
                    break;
                case "UPDATE": updateItem(request, response);
                    break;
                case "DELETE": deleteItem(request, response);
                    break;
                case "LOGOUT": logout(request, response);
                    break;
                default:
                    listItems(request, response);
            }
        }catch(Exception e){
            throw new ServletException(e);
        }
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String command = request.getParameter("command");
        //assign 'LIST' command if null
        command = command != null ? command : "LIST";
        
        try {
            switch (command) {
                case "LIST": listItems(request, response);
                    break;
                case "ADD": addItem(request, response);
                    break;
                default:
                    listItems(request, response);
            }
        }catch(Exception e){
            throw new ServletException(e);
        }
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
        
        try {
              //get all the items the convert it to json string
              Gson gson = new Gson();
              List<Item> items = dbUtil.getAllItems();
              String result = gson.toJson(items);
              
              for(Item item : items) {
                  System.out.println("item name->" + item.getName() + "\t" + item.getPrice());
              }
              
              // then send the response back to javascript
              PrintWriter out = response.getWriter();
              response.setHeader("Content-Type", "application/json");
              System.out.println("result" + result);
              out.println(result);
            
        }catch(Exception e){
            throw new ServletException(e);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        Boolean user = (Boolean) request.getSession().getAttribute("is_login");
        
        if (user != null) {
            request.getSession().removeAttribute("is_login");
        }
        
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String itemParam = request.getParameter("param");
        
        Gson gson = new Gson();
        Item item = gson.fromJson(itemParam, Item.class);
        
        dbUtil.addItem(item);
        
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "application/json");
        out.println(gson.toJson(item, Item.class));
    }

    private void loadItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String itemId = request.getParameter("itemId");
        
        Item item = dbUtil.loadItem(Integer.valueOf(itemId));
        
        request.setAttribute("item", item);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-item-page.jsp");
        dispatcher.forward(request, response);
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
       
        String id = request.getParameter("itemId");
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        
        Item item = new Item();
        item.setId(Integer.valueOf(id));
        item.setName(name);
        item.setCode(code);
        item.setDescription(description);
        item.setPrice(new BigDecimal(price));
        item.setStock(Integer.valueOf(stock));
        
        dbUtil.updateItem(item);
        
        response.sendRedirect(request.getContextPath() + "/SystemController");
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) 
        throws Exception{
        
        String itemId = request.getParameter("itemId");
        
        dbUtil.deleteItem(Integer.valueOf(itemId));
        
        response.sendRedirect(request.getContextPath() + "/SystemController");
    }

}
