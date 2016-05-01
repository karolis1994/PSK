/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Filters;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
/**
 *
 * @author donatas
 */

@WebFilter(filterName = "Authentication",
urlPatterns = {""})

public class Authentication extends HttpServlet implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
         HttpServletRequest httpRequest  = (HttpServletRequest) request;
         Object authUserEmail = httpRequest.getSession().getAttribute("authUserEmail");
         if(authUserEmail == null)
         {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");   
         }
         else
         {
            chain.doFilter(request, response);
         }
    }
}
