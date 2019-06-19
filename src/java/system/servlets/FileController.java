/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import system.util.Console;

/**
 *
 * @author marwin
 */
@WebServlet(name = "FileController", urlPatterns = {"/FileController"})
public class FileController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Resource(name="jdbc/inventory_sys")
    private DataSource datasource;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Console.log("Inside FileController");
        
        Connection c = null;
        try {
            
            c = datasource.getConnection();
            String jrxml = request.getServletContext().getRealPath("assets/reports/test.jrxml");
            Console.log("is file existing?", new File(jrxml).exists());
            InputStream input = new FileInputStream(new File(jrxml));
            
            //Generating report
            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, c);
            
            SimpleExporterInput in = new SimpleExporterInput(jasperPrint);
            SimpleOutputStreamExporterOutput out = new SimpleOutputStreamExporterOutput(response.getOutputStream());
            response.setCharacterEncoding("UTF-8");
            
            //Exporting the report as PDF
            response.setContentType("application/pdf");
            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(in);
            pdfExporter.setExporterOutput(out);
            
            pdfExporter.exportReport();
            
            response.getOutputStream().flush();
            response.getOutputStream().close();
            
        
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(c != null) {
                c.close();
            }
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
        try {
            processRequest(request, response);
        }catch(SQLException e) {
            e.printStackTrace();
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
        try {
            processRequest(request, response);
        }catch(SQLException e) {
            e.printStackTrace();
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

}
