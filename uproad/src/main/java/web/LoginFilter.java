package web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.ServiceType;
import dao.User;
import dao.VehicleCategory;
import dbc.DBConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*") // This filter will be applied to all requests
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	
    	Logger logger = LoggerFactory.getLogger(LoginFilter.class);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Get session if it exists

        boolean isLoggedIn = session != null && session.getAttribute("userLoggedIn")!= null;
        
        String uri = httpRequest.getRequestURI();
        //logger.info(uri);
        if(isLoggedIn)
        	logger.info("{} - {}", ((User) session.getAttribute("user")).getUsername(), uri);
        else 
        	logger.info(uri);
       
        List<String> allowedPaths = Arrays.asList("/uproad/", "/uproad/LoginServlet", "/uproad/SignupServlet", "/uproad/VehicleCategoryServlet", "/uproad/landing.html", "/uproad/login.jsp");
        
        // Allow static resources by file extension
        boolean isStaticResource = uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png")
                || uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".gif")
                || uri.endsWith(".woff") || uri.endsWith(".ttf") || uri.endsWith(".ico") || uri.endsWith(".svg");

        
        if (isLoggedIn || allowedPaths.contains(uri) || isStaticResource) {
            // Set common attributes
            ArrayList<VehicleCategory> catList = new DBConnection().getVehicleCategory();
            request.setAttribute("categories", catList);

            ArrayList<ServiceType> serviceList = new DBConnection().getServiceTypes();
            request.setAttribute("services", serviceList);

            ArrayList<String> allCities = new DBConnection().getAllCities();
            request.setAttribute("cities", allCities);

            ArrayList<String> allHighways = new DBConnection().getAllHighways();
            request.setAttribute("highways", allHighways);

            chain.doFilter(request, response);
            
        } else {
            // Not logged in and trying to access protected resource → redirect to login page
            httpResponse.sendRedirect("/uproad/");
        }
    
    }

    @Override
    public void destroy() {
        // Filter destruction if needed
    }
}
