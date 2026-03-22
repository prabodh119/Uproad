package web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.User;
import dbc.DBConnection;
import util.TokenUtil;

import java.io.IOException;
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
        
        String uri = httpRequest.getRequestURI();
        String usernameForMDC = "undefined";   // default
        String sourceForMDC = "undefined";   // default
        
        try {
        	// ---------------------------
            // 1. WEB USER (session login)
            // ---------------------------
        	
	        boolean isLoggedIn = session != null && session.getAttribute("userLoggedIn")!= null;
	       
	        if (isLoggedIn) {
	            User user = (User) session.getAttribute("user");
	            if (user != null) {
	                usernameForMDC = user.getUsername();
	                sourceForMDC = "web";    // Mark as WEB
	            }
	        }

	        // ---------------------------
	        // 2. API USER (JWT / Bearer token)
	        // ---------------------------
	        
	        String authHeader = httpRequest.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);

	            if (TokenUtil.isValid(token)) {
	                String tokenUser = TokenUtil.validateToken(token);  // ← implement this
	                if (tokenUser != null) {
	                    usernameForMDC = tokenUser;
	                    sourceForMDC = "mobile";  // Mark as APP
	                }
	            }
	        }
	        
	        //MDC.put("username", isLoggedIn ? ((User)session.getAttribute("user")).getUsername() : "");
	        //ThreadContext.put("username",isLoggedIn ? ((User) session.getAttribute("user")).getUsername() : "");
	        
	        // Add username to MDC / ThreadContext
	        ThreadContext.put("username", usernameForMDC);
	        ThreadContext.put("source", sourceForMDC);
	        
	        logger.info("Request: "+uri);
	       
	        List<String> allowedPaths = Arrays.asList(
	        		"/", 
	        		"/login.jsp",
	        		"/landing.html",
	        		"/LoginServlet", 
	        		"/SignupServlet", 
	        		"/verifyEmail",
	        		"/forgotPassword",
	        		"/ForgotPasswordServlet",
	        		"/resetPassword.jsp",
	        		"/ResetPasswordServlet",
	        		"/test.html",
	        		"/privacy-policy.html",
	        		"/terms-and-conditions.html"
	        );
	        
	        // Allow static resources by file extension
	        boolean isStaticResource = uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png")
	                || uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".gif")
	                || uri.endsWith(".woff") || uri.endsWith(".ttf") || uri.endsWith(".ico") || uri.endsWith(".svg");
	
	        // API request detection
	        boolean isApiRequest = uri.startsWith("/api/");
	        boolean isPublicApi = uri.equals("/api/login") || uri.equals("/api/register") ||uri.equals("/api/vehicleCategories")
	        		|| uri.equals("/api/users/verify") || uri.equals("/api/forgotPassword") || uri.equals("/api/resetPassword");
	        //boolean isLoginApi = uri.equals("/uproad/api/login");
	        
	        if (isApiRequest) {
	            if (isPublicApi) {
	                // Login API is always allowed
	            	chain.doFilter(request, response);
	            } else {
	                // Check for Bearer token
	                //String authHeader = httpRequest.getHeader("Authorization");
	                if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                    String token = authHeader.substring(7);
	                    if (TokenUtil.isValid(token)) {
	                        chain.doFilter(request, response);
	                        return;
	                    }
	                }
	
	                // Unauthorized API call
	                httpResponse.setContentType("application/json");
	                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                httpResponse.getWriter().write("{\"success\": false, \"message\": \"Unauthorized\"}");
	                return;
	            } 
	            
	        } else if (isLoggedIn || allowedPaths.contains(uri) || isStaticResource) {
	            // Set common attributes
	            request.setAttribute("categories", new DBConnection().getVehicleCategory());
	            request.setAttribute("services", new DBConnection().getServiceTypes());
	            request.setAttribute("cities", new DBConnection().getAllCities());
	            request.setAttribute("highways", new DBConnection().getAllHighways());
	
	            chain.doFilter(request, response);
	            
	        } else {
	            // Not logged in and trying to access protected resource → redirect to login page
	            httpResponse.sendRedirect("/");
	        }
        } finally {
        	// IMPORTANT: clear MDC for next request thread
            ThreadContext.clearAll();
		}
		
    }

    @Override
    public void destroy() {
        // Filter destruction if needed
    }
}
