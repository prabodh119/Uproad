package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
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
 * Servlet implementation class ApiSignupServlet
 */
@WebServlet("/api/register")
public class ApiSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiSignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Optionally reject GET requests
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("{\"error\":\"POST method required\"}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Logger logger = LoggerFactory.getLogger(ApiSignupServlet.class);
		
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        
        // Build JSON response
        JSONObject resJson = new JSONObject();

        try {
            // Read JSON request body
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JSONObject reqJson = new JSONObject(sb.toString());

            // Extract parameters from JSON
            String name = reqJson.getString("name");
            String telephone = reqJson.getString("telephone");
            String email = reqJson.getString("email"); // Used as username
            String password = reqJson.getString("password");
            String vehicleMake = reqJson.getString("vehicleMake");
            String vehicleModel = reqJson.getString("vehicleModel");
            String vehicleYear = reqJson.getString("vehicleYear");
            String vehicleCategory = reqJson.getString("vehicleCategory");
            
            logger.info("Signup request received from app user : " + email);

            // Encrypt password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            
            // Generate token
            String token = UUID.randomUUID().toString();

            // Save to database using your existing method
            DBConnection dbc = new DBConnection();
            dbc.insertUser(email, hashedPassword, telephone, name, vehicleMake, vehicleModel, vehicleYear, vehicleCategory, token);

            String verifyLink = Config.getBaseUrl()+"/verifyEmail?token=" + token;
            String verifyDeepLink = Config.getEmailVerifyDeeplink() + "?token=" + token;
            
            EmailService.sendVerificationEmail(email, verifyLink, verifyDeepLink);
            
            // Everything succeeded
            resJson.put("success", true);
            resJson.put("message", "Registration successful! Please check your email to verify your account.");
            response.setStatus(HttpServletResponse.SC_OK);
            logger.info("user registratoin successful for app user : "+email);
            
        } catch (DuplicateUserException e) {
        	resJson.put("success", false);
            resJson.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK); 
            e.printStackTrace();

        } catch (UserDatabaseException e) {
            resJson.put("success", false);
            resJson.put("message", "Database error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            e.printStackTrace();

        }catch (EmailException e) {
            resJson.put("success", false);
            resJson.put("message", "User registered successfully, but failed to send verification email");
            response.setStatus(HttpServletResponse.SC_OK); 
            e.printStackTrace();

        }  catch (JSONException e) {
            resJson.put("success", false);
            resJson.put("message", "Invalid JSON input: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            e.printStackTrace();
            
        } catch (Exception e) {
            resJson.put("success", false);
            resJson.put("message", "Unexpected server error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            e.printStackTrace();
        }
        out.write(resJson.toString());
    }

}
