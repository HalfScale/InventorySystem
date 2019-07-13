/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRParameter;
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
@WebServlet(name = "JasperReportPrint", urlPatterns = {"/JasperReportPrint"})
public class JasperReportPrint extends HttpServlet {

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
        
        Connection c = null;
        final String filename = request.getParameter("filename");
        final String module = request.getParameter("module");
        final String jrxml = request.getServletContext().getRealPath("WEB-INF/reports/" + module  + "/" +  filename + ".jrxml");
        
        try {
            c = datasource.getConnection();
            Console.log("is file existing?", new File(jrxml).exists());
            InputStream input = new FileInputStream(new File(jrxml));
            
            
            //Generating report
            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            
            //Get additional parameters for the report
            Map<String, Object> parameters = getParameters(request);
            
            if(parameters.isEmpty()) {
                Console.log("Parameters are empty");
            }
            for(Map.Entry entry: parameters.entrySet()) {
                Console.log("key:", entry.getKey(), "value:", entry.getValue());
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, c);
            
            
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
    
    private Map getParameters(HttpServletRequest request) {
        String params = request.getParameter("params");
        Map reportParams = new HashMap();
        
        if (params != null && !params.trim().isEmpty()) {
            JsonObject obj = new JsonParser().parse(params).getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry: obj.entrySet()) {
                reportParams.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        
        return reportParams;
    }
}
