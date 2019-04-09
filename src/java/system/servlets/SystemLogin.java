/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SystemLogin</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SystemLogin at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        
        String user = request.getParameter("username"); 
        String pass = request.getParameter("password"); 
        
        try {
            // query the given user and pass
            User activeUser = dbUtil.getUser(user, pass);
            
            if(activeUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("active_user", activeUser);
                System.out.println("User " + activeUser.getName() + " logged in.");
                
                registerSystemLog(request, response, LogType.LOGIN);
                response.sendRedirect(request.getContextPath() + "/system-home/home.jsp");
            }else {
                System.out.println("Incorrect user and password");
                response.sendRedirect(request.getContextPath() + "/");
            }
            //then check if its a valid user
        }catch(Exception e) {
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
    
    private void registerSystemLog(HttpServletRequest request, HttpServletResponse response, int type) 
        throws Exception {
        HttpSession session = request.getSession(false);
        
        if(session != null) {
            User user = (User) request.getSession(false).getAttribute("active_user");
            dbUtil.registerLog(type, user);
            System.out.println("User: " + user.getName());
        }else {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }

}
