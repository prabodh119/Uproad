package web;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbc.DBConnection;
import exc.DuplicateUserException;
import exc.EmailException;
import exc.UserDatabaseException;
import util.Config;
import util.EmailService;

/**
 * Servlet implementation class SignupServlet
 */
@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Logger logger = LoggerFactory.getLogger(SignupServlet.class);
		
		String name = request.getParameter("name");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email"); // Used as username
        String password = request.getParameter("password"); // Get password input
        String vehicleMake = request.getParameter("vehicleMake");
        String vehicleModel = request.getParameter("vehicleModel");
        String vehicleYear = request.getParameter("vehicleYear");
        String vehicleCategory = request.getParameter("vehicleCategory");
        
        logger.info("Signup request received from web user : " + email);
        
        // Encrypt the password before saving it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        
        // Generate token
        String token = UUID.randomUUID().toString();

        // Here, insert the data (including hashedPassword) into the database
        
	    try {
	    	DBConnection dbc = new DBConnection();
	    	dbc.insertUser(email, hashedPassword, telephone, name, vehicleMake, vehicleModel, vehicleYear, vehicleCategory, token);
	    	
	    	// Send verification email
            String verifyLink = Config.getBaseUrl()+"/verifyEmail?token=" + token;
            EmailService.sendVerificationEmail(email, verifyLink);
            
            request.setAttribute("message", "Registration successful! Please check your email to verify your account.");
            logger.info("user registratoin successful for web user : "+email);
            
	    } catch (DuplicateUserException e) {
	    	request.setAttribute("message", e.getMessage());
	    	e.printStackTrace();
	    	
		} catch (UserDatabaseException  e) {
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
			
		} catch (EmailException e) {
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
			
		} catch (Exception e) {
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
		}
		/*
		 * int result = dbc.insertUser(email, hashedPassword, telephone, name,
		 * vehicleMake, vehicleModel, vehicleYear, vehicleCategory, token);
		 * 
		 * if(result > 0) { // Send verification email String verifyLink =
		 * Config.getBaseUrl()+"/VerifyEmailServlet?token=" + token; Boolean
		 * emailSuccess = EmailService.sendVerificationEmail(email, verifyLink);
		 * 
		 * if (emailSuccess) request.setAttribute("message",
		 * "Registration successful! Please check your email to verify your account.");
		 * 
		 * else request.setAttribute("message",
		 * "Registration successful! Could not send the verification email !!");
		 * 
		 * } else request.setAttribute("message", "Registration Failed !");
		 */
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
