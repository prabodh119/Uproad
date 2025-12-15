package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.User;
import dbc.DBConnection;
import util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ApiLoginServlet
 */
@WebServlet("/api/login")
public class ApiLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Logger logger = LoggerFactory.getLogger(ApiLoginServlet.class);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject resJson = new JSONObject();
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

            JSONObject reqJson = new JSONObject(sb.toString());

            // 2️ Extract input fields
            String username = reqJson.optString("username", "").trim();
            String password = reqJson.optString("password", "").trim();

            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Username or password cannot be empty");
            }

            logger.info("Login attempt for username: {}", username);

            // 3️ Validate credentials against DB
            DBConnection dbc = new DBConnection();
            User loggedInUser;
            try {
                loggedInUser = dbc.isValidUser(username, password);
            } catch (Exception dbEx) {
                logger.error("Database error during login for user {}: {}", username, dbEx.getMessage());
                throw new RuntimeException("Internal server error during login");
            }

            // 4️ Build response
            if (loggedInUser != null) {
                // Generate JWT token
                String token;
                try {
                    token = TokenUtil.generateToken(loggedInUser.getUsername());
                } catch (Exception tokenEx) {
                    logger.error("Token generation failed for user {}: {}", username, tokenEx.getMessage());
                    throw new RuntimeException("Failed to generate authentication token");
                }

                resJson.put("success", true);
                resJson.put("token", token);
                resJson.put("username", loggedInUser.getUsername());
                resJson.put("name", loggedInUser.getName());
                response.setStatus(HttpServletResponse.SC_OK);

                logger.info("Login successful for user: {}", username);
            } else {
                resJson.put("success", false);
                resJson.put("message", "Invalid username or password");
                response.setStatus(HttpServletResponse.SC_OK);
                logger.warn("Login failed for username: {}", username);
            }

        } catch (IllegalArgumentException e) {
            //  Client-side input errors
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resJson.put("success", false);
            resJson.put("message", e.getMessage());
            logger.warn("Bad login request: {}", e.getMessage());

        } catch (JSONException e) {
            //  JSON parsing issues
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resJson.put("success", false);
            resJson.put("message", "Malformed JSON request");
            logger.error("Malformed JSON in login request", e);

        } catch (RuntimeException e) {
            //  Controlled server-side issues
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resJson.put("success", false);
            resJson.put("message", e.getMessage());
            logger.error("Application error during login", e);

        } catch (Exception e) {
            //  Unexpected errors
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resJson.put("success", false);
            resJson.put("message", "Unexpected error occurred");
            logger.error("Unhandled exception in login API", e);

        } finally {
            //  Ensure consistent JSON response
            out.write(resJson.toString());
            out.flush();
            out.close();
        }
    }



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
	    response.getWriter().write("{\"error\":\"GET method not allowed\"}");
	}

}
