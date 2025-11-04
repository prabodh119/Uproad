package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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

/**
 * Servlet implementation class ApiResetPasswordServlet
 */
@WebServlet("/api/resetPassword")
public class ApiResetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApiResetPasswordServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
	    response.getWriter().write("{\"error\":\"GET method not allowed\"}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    Logger logger = LoggerFactory.getLogger(ApiResetPasswordServlet.class);

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    JSONObject jsonResponse = new JSONObject();
	    PrintWriter out = response.getWriter();

	    try {
	        // 1️ Read JSON body safely
	        StringBuilder sb = new StringBuilder();
	        try (BufferedReader reader = request.getReader()) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	        }

	        if (sb.length() == 0) {
	            throw new IllegalArgumentException("Empty request body");
	        }

	        JSONObject reqJson = new JSONObject(sb.toString());

	        // 2️ Extract and validate inputs
	        String token = reqJson.optString("token", "").trim();
	        String newPassword = reqJson.optString("password", "").trim();

	        if (token.isEmpty()) {
	            throw new IllegalArgumentException("Missing or empty token");
	        }
	        if (newPassword.isEmpty()) {
	            throw new IllegalArgumentException("Missing or empty password");
	        }

	        logger.info("Password reset request received for token: {}", token);

	        // 3️ Encrypt password
	        String hashedPassword;
	        try {
	            hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
	        } catch (Exception e) {
	            logger.error("Password hashing failed", e);
	            throw new RuntimeException("Error while securing the password");
	        }

	        // 4️ Validate token and update password
	        DBConnection dbc = new DBConnection();
	        boolean tokenValid;
	        try {
	            tokenValid = dbc.isValidResetToken(token);
	        } catch (Exception dbEx) {
	            logger.error("Database error during token validation", dbEx);
	            throw new RuntimeException("Internal server error during token validation");
	        }

	        if (!tokenValid) {
	            jsonResponse.put("success", false);
	            jsonResponse.put("message", "Invalid or expired token");
	            response.setStatus(HttpServletResponse.SC_OK);
	            logger.warn("Invalid or expired token used for password reset: {}", token);
	        } else {
	            try {
	                boolean updated = dbc.updatePasswordWithToken(token, hashedPassword);
	                if (updated) {
	                    jsonResponse.put("success", true);
	                    jsonResponse.put("message", "Password has been reset successfully");
	                    response.setStatus(HttpServletResponse.SC_OK);
	                    logger.info("Password reset successful for token: {}", token);
	                } else {
	                    jsonResponse.put("success", false);
	                    jsonResponse.put("message", "Failed to reset password");
	                    response.setStatus(HttpServletResponse.SC_OK);
	                    logger.warn("Database update failed for password reset token: {}", token);
	                }
	            } catch (Exception dbEx) {
	                logger.error("Database error during password update", dbEx);
	                throw new RuntimeException("Internal server error during password update");
	            }
	        }

	    } catch (IllegalArgumentException e) {
	        //  Input validation errors (client issue)
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", e.getMessage());
	        logger.warn("Bad request in reset-password API: {}", e.getMessage());

	    } catch (JSONException e) {
	        //  Invalid JSON format
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", "Malformed JSON request");
	        logger.error("Malformed JSON in reset-password request", e);

	    } catch (RuntimeException e) {
	        //  Controlled internal issues
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", e.getMessage());
	        logger.error("Application error in reset-password API", e);

	    } catch (Exception e) {
	        //  Unexpected errors
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", "Unexpected error occurred");
	        logger.error("Unhandled exception in reset-password API", e);

	    } finally {
	        //  Ensure response is always sent
	        out.write(jsonResponse.toString());
	        out.flush();
	        out.close();
	    }
	}


}
