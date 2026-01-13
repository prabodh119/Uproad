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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbc.DBConnection;

/**
 * Servlet implementation class ApiVerifyEmail
 */
@WebServlet("/api/users/verify")
public class ApiVerifyEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiVerifyEmail() {
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
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    Logger logger = LoggerFactory.getLogger(ApiVerifyEmail.class);

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    JSONObject resJson = new JSONObject();
	    PrintWriter out = response.getWriter();

	    try {
	        // 1 Read JSON body safely
	        StringBuilder sb = new StringBuilder();
	        String line;
	        try (BufferedReader reader = request.getReader()) {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	        }

	        if (sb.length() == 0) {
	            throw new IllegalArgumentException("Empty request body");
	        }

	        JSONObject reqJson = new JSONObject(sb.toString());

	        // 2️ Extract token
	        String token = reqJson.optString("token", null);
	        if (token == null || token.trim().isEmpty()) {
	            throw new IllegalArgumentException("Missing or empty token");
	        }

	        logger.info("Email verification request from app user with token: {}", token);

	        // 3️ Perform verification
	        DBConnection dbc = new DBConnection();
	        boolean verified;

	        try {
	            verified = dbc.verifyUser(token);
	        } catch (Exception dbEx) {
	            logger.error("Database error while verifying token: {}", token, dbEx);
	            throw new RuntimeException("Internal server error during verification");
	        }

	        // 4️ Build response
	        if (verified) {
	            resJson.put("success", true);
	            resJson.put("message", "Email verified successfully. Please log in.");
	            response.setStatus(HttpServletResponse.SC_OK);
	            logger.info("Email verification success for app user with token: {}", token);
	        } else {
	            resJson.put("success", false);
	            resJson.put("message", "Invalid or expired verification link");
	            response.setStatus(HttpServletResponse.SC_OK);
	            logger.warn("Email verification failed (invalid/expired token): {}", token);
	        }

	    } catch (IllegalArgumentException e) {
	        //  Handle invalid input (bad request)
	        logger.warn("Invalid request: {}", e.getMessage());
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        resJson.put("success", false);
	        resJson.put("message", e.getMessage());

	    } catch (JSONException e) {
	        //  Handle JSON parsing errors
	        logger.error("Malformed JSON in request body", e);
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        resJson.put("success", false);
	        resJson.put("message", "Malformed JSON request");

	    } catch (RuntimeException e) {
	        // Handle database or internal logic errors
	        logger.error("Unexpected server error", e);
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resJson.put("success", false);
	        resJson.put("message", "Internal server error");

	    } catch (Exception e) {
	        //  Catch-all fallback
	        logger.error("Unhandled exception", e);
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resJson.put("success", false);
	        resJson.put("message", "Unexpected error occurred");

	    } finally {
	        // Always send JSON response
	        out.write(resJson.toString());
	        out.flush();
	        out.close();
	    }
	}


}
