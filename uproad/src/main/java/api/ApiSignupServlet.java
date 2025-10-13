package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import dbc.DBConnection;

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
	
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            // 🔹 Read JSON request body
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JSONObject reqJson = new JSONObject(sb.toString());

            // 🔹 Extract parameters from JSON
            String name = reqJson.getString("name");
            String telephone = reqJson.getString("telephone");
            String idNumber = reqJson.getString("idNumber"); // Used as username
            String password = reqJson.getString("password");
            String vehicleMake = reqJson.getString("vehicleMake");
            String vehicleModel = reqJson.getString("vehicleModel");
            String vehicleYear = reqJson.getString("vehicleYear");
            String vehicleCategory = reqJson.getString("vehicleCategory");

            // 🔹 Encrypt password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // 🔹 Save to database using your existing method
            DBConnection dbc = new DBConnection();
            int result = dbc.insertUser(idNumber, hashedPassword, telephone, name, vehicleMake, vehicleModel, vehicleYear, vehicleCategory);

            // 🔹 Build JSON response
            JSONObject resJson = new JSONObject();
            if (result > 0) {
                resJson.put("success", true);
                resJson.put("message", "Successfully Registered. Please Login.");
            } else {
                resJson.put("success", false);
                resJson.put("message", "Registration Failed!");
            }

            out.write(resJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject error = new JSONObject();
            error.put("success", false);
            error.put("message", "Server Error: " + e.getMessage());
            out.write(error.toString());
        }
    }

}
