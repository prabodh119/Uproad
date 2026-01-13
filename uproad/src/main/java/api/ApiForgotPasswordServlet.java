package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbc.DBConnection;
import util.Config;
import util.EmailService;

/**
 * Servlet implementation class ApiForgotPasswordServlet
 */
@WebServlet("/api/forgotPassword")
public class ApiForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiForgotPasswordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
	    response.getWriter().write("{\"error\":\"GET method not allowed\"}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    Logger logger = LoggerFactory.getLogger(ApiForgotPasswordServlet.class);

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    JSONObject jsonResponse = new JSONObject();
	    PrintWriter out = response.getWriter();

	    try {
	        // 1️ Read and validate JSON body
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

	        JSONObject requestBody = new JSONObject(sb.toString());
	        String email = requestBody.optString("email", "").trim();

	        if (email.isEmpty()) {
	            throw new IllegalArgumentException("Missing or empty email");
	        }

	        logger.info("Password reset request received from app user: {}", email);

	        // 2️ Generate token & expiry
	        String token = UUID.randomUUID().toString();
	        String expiry = LocalDateTime.now()
	                .plusMinutes(30)
	                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

	        // 3️ Save token in DB
	        DBConnection dbc = new DBConnection();
	        boolean saved;
	        try {
	            saved = dbc.saveResetToken(email, token, expiry);
	        } catch (Exception dbEx) {
	            logger.error("Database error while saving reset token for {}: {}", email, dbEx.getMessage());
	            throw new RuntimeException("Internal server error while processing reset request");
	        }

	        // 4️ Respond according to result
	        if (saved) {
	            String resetLink = Config.getBaseUrl() + "/resetPassword.jsp?token=" + token;
	            String resetDeepLink = Config.getForgotPassDeeplink() + "?token=" + token;

	            boolean emailSent;
	            try {
	                emailSent = EmailService.sendPasswordResetEmail(email, resetLink, resetDeepLink);
	            } catch (Exception mailEx) {
	                logger.error("Failed to send reset email to {}: {}", email, mailEx.getMessage());
	                throw new RuntimeException("Failed to send reset email");
	            }

	            if (emailSent) {
	                jsonResponse.put("success", true);
	                jsonResponse.put("message", "Password reset link sent to your email");
	                response.setStatus(HttpServletResponse.SC_OK);
	                logger.info("Password reset email sent successfully to {}", email);
	            } else {
	                jsonResponse.put("success", false);
	                jsonResponse.put("message", "Could not send the reset email");
	                response.setStatus(HttpServletResponse.SC_OK);
	                logger.warn("Password reset email failed for {}", email);
	            }

	        } else {
	            jsonResponse.put("success", false);
	            jsonResponse.put("message", "Email not found in records");
	            response.setStatus(HttpServletResponse.SC_OK);
	            logger.warn("Password reset requested for non-existing email: {}", email);
	        }

	    } catch (IllegalArgumentException e) {
	        //  Handle client-side validation issues
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", e.getMessage());
	        logger.warn("Bad request in forgot-password API: {}", e.getMessage());

	    } catch (JSONException e) {
	        //  Handle malformed JSON
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", "Malformed JSON request");
	        logger.error("Malformed JSON in forgot-password request", e);

	    } catch (RuntimeException e) {
	        //  Handle controlled internal errors
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", e.getMessage());
	        logger.error("Application error in forgot-password API", e);

	    } catch (Exception e) {
	        //  Catch-all for unexpected issues
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        jsonResponse.put("success", false);
	        jsonResponse.put("message", "Unexpected error occurred");
	        logger.error("Unhandled exception in forgot-password API", e);

	    } finally {
	        // Ensure response is always sent
	        out.write(jsonResponse.toString());
	        out.flush();
	        out.close();
	    }
	}

}
