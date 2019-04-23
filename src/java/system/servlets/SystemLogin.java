/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
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
import system.util.DbUtil;
import system.valueobject.LogType;
import system.valueobject.User;

/**
 *
 * @author Muffin
 */
@WebServlet(name = "SystemLogin", urlPatterns = {"/SystemLogin"})
public class SystemLogin extends HttpServlet {
    
    @Resource(name="jdbc/inventory_sys")
    DataSource datasource;
    DbUtil dbUtil;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        
        dbUtil = new DbUtil(datasource);
    }
    
    

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
        
        Map result = new HashMap();
        Gson gson = new Gson();
        String user = request.getParameter("username"); 
        String pass = request.getParameter("password"); 
        
        try {
            // query the given user and pass
            User activeUser = dbUtil.getUser(user, pass);
            
            if(activeUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("active_user", activeUser);
                Console.log("User:", activeUser.getName());
                
                registerSystemLog(request, response, LogType.LOGIN);
                result.put("status", 0);
                result.put("message", "Login successful!");
//                response.sendRedirect(request.getContextPath() + "/system-home/home.jsp");
            }else {
                Console.log("Incorrect user and password");
                result.put("status", 1);
                result.put("message", "Incorrect user and password...");
            }
            //then check if its a valid user
        }catch(ServletException e) {
            result.put("status", 1);
            result.put("message", e.getMessage());
            
        }catch(SQLException e) {
            result.put("status", 1);
            result.put("message", e.getMessage());
            
        }catch(Exception e) {
            result.put("status", 1);
            result.put("message", e.getMessage());
        
        }
        
        response.setHeader("Content-Type", "application/json");
        PrintWriter out = response.getWriter();
        String data = gson.toJson(result);
        Console.log("data", data);
        out.println(data);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    
    private void registerSystemLog(HttpServletRequest request, HttpServletResponse response, int type) 
        throws Exception {
        HttpSession session = request.getSession(false);
        
        if(session != null) {
            User user = (User) request.getSession(false).getAttribute("active_user");
            dbUtil.registerLog(type, user);
            Console.log("User:", user.getName());
        }else {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }

}
