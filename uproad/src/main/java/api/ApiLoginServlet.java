package api;

import java.io.BufferedReader;
import java.io.IOException;
import org.json.JSONObject;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Read JSON body from Android request
        	// Read JSON body from request
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        	
            JSONObject reqJson = new JSONObject(sb.toString());
            
            String username = reqJson.getString("username");
            String password = reqJson.getString("password");

            // Validate against DB
            DBConnection dbc = new DBConnection();
            User loggedInUser = dbc.isValidUser(username, password);

            if (loggedInUser != null) {
            	// ✅ Generate JWT token
                String token = TokenUtil.generateToken(loggedInUser.getUsername());
                
                JSONObject resJson = new JSONObject();
                
                resJson.put("success", true);
                resJson.put("token", token);
                resJson.put("username", loggedInUser.getUsername());
                resJson.put("name", loggedInUser.getName());
                
                response.getWriter().write(resJson.toString());
            } else {
            	// ❌ Invalid credentials
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                
                JSONObject resJson = new JSONObject();
                resJson.put("success", false);
                resJson.put("message", "Invalid username or password");
                
                response.getWriter().write(resJson.toString());
            }

        } catch (Exception e) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	
            JSONObject resJson = new JSONObject();
            resJson.put("success", false);
            resJson.put("message", "Invalid request format");
            resJson.put("error", e.getMessage());
            
            response.getWriter().write(resJson.toString());
        }
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
