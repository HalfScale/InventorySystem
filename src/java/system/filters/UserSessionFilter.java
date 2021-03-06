/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import system.util.Console;
import system.valueobject.User;

/**
 *
 * @author Muffin
 */
public class UserSessionFilter implements Filter {
    
    private String contextPath;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
        System.out.println("context path " + contextPath);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
//        Console.log("session is not null?", session != null);
//        Console.log("session attribute is not null?", session.getAttribute("active_user") != null);
        
        if (session != null && session.getAttribute("active_user") != null) {
            fc.doFilter(request, response);
        }else {
            httpResponse.sendRedirect(contextPath + "/");
            Console.log("No user is existing in this session");
        }
    
    }
    
    @Override
    public void destroy() {
    
    }
}
