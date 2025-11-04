package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.Garage;
import dbc.DBConnection;
import util.TokenUtil;

/**
 * Servlet implementation class ApiSearchServlet
 */
@WebServlet("/api/garage/search")
public class ApiSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Validate JWT from Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Missing or invalid token\"}");
                return;
            }

            String token = authHeader.substring(7);
            String tokenUsername = TokenUtil.validateToken(token);
            if (tokenUsername == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Invalid or expired token\"}");
                return;
            }

            // 2. Parse request JSON
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            JSONObject reqJson = new JSONObject(sb.toString());

            String username = tokenUsername;
            String vehicleCategory = reqJson.getString("vehicleCategory");
            JSONArray serviceTypesJson = reqJson.getJSONArray("serviceTypes");
            
            String[] serviceTypes = new String[serviceTypesJson.length()];
            for (int i = 0; i < serviceTypesJson.length(); i++) {
                serviceTypes[i] = serviceTypesJson.getString(i);
            }
            String nearestCity = reqJson.getString("nearestCity");

            // 3. Check token username matches request username
            if (!username.equals(tokenUsername)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Username does not match token\"}");
                return;
            }

            // 4. Query DB
            DBConnection dbc = new DBConnection();
            List<Garage> garages = dbc.getGaragebyServiceAndcity(username, vehicleCategory, serviceTypes, nearestCity);

            // 5. Build JSON response
            JSONArray garageArray = new JSONArray();
            for (Garage g : garages) {
                JSONObject gj = new JSONObject();
                gj.put("index", g.getIndex());
                gj.put("name", g.getName());
                gj.put("address", g.getAddress());
                gj.put("contact_no", g.getContactNo());
                gj.put("contact_2", g.getContactNo2());
                gj.put("contact_3", g.getContactNo3());
                gj.put("near_city", g.getNearCity());
                gj.put("highway", g.getRoad());
                gj.put("website", g.getWebsite());
                gj.put("vehicle_category", g.getVehicle_category());
                gj.put("services", g.getServices());
                gj.put("field12", g.getField12());
                gj.put("field13", g.getField13());
                gj.put("field14", g.getField14());
                gj.put("field15", g.getField15());
                garageArray.put(gj);
            }

            JSONObject resJson = new JSONObject();
            resJson.put("success", true);
            resJson.put("garages", garageArray);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(resJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Invalid request");
            err.put("error", e.getMessage());
            response.getWriter().write(err.toString());
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
