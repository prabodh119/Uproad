package api;

import dao.User;
import dao.VehicleDetail;
import dbc.DBConnection;
import util.TokenUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class ApiUserProfileServlet
 */
@WebServlet("/api/user/profile")
public class ApiUserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiUserProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // 1. Read Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"error\":\"Missing or invalid Authorization header\"}");
                return;
            }

            String token = authHeader.substring(7); // remove "Bearer "

            // 2. Validate token
            String username = TokenUtil.validateToken(token);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"error\":\"Invalid or expired token\"}");
                return;
            }

            // 3. Fetch user from DB
            DBConnection dbc = new DBConnection();
            User user = dbc.getUserByUsername(username); // implement this in DBConnection
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\":\"User not found\"}");
                return;
            }

            // 4. Build JSON response
            JSONObject json = new JSONObject();
            json.put("username", user.getUsername());
            json.put("name", user.getName());
            json.put("telephone", user.getTelephone());

            // Vehicles
            JSONArray vehiclesArr = new JSONArray();
            List<VehicleDetail> vehicles = user.getVehicles();
            if (vehicles != null) {
                for (VehicleDetail v : vehicles) {
                    JSONObject vJson = new JSONObject();
                    vJson.put("id", v.getId());
                    vJson.put("make", v.getMake());
                    vJson.put("model", v.getModel());
                    vJson.put("year", v.getYear());
                    vJson.put("category", v.getVehicleCategory());
                    vJson.put("nickname", v.getNickname());
                    vehiclesArr.put(vJson);
                }
            }
            json.put("vehicles", vehiclesArr);

            // 5. Send response
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Server error\"}");
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
