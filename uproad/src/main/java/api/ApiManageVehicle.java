package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.User;
import dao.VehicleDetail;
import dbc.DBConnection;
import util.TokenUtil;

/**
 * Servlet implementation class ApiAddVehicle
 */
@WebServlet("/api/ApiManageVehicle")
public class ApiManageVehicle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiManageVehicle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        Logger logger = LoggerFactory.getLogger(ApiManageVehicle.class);
        
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
            
            // 3. Parse request JSON
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            JSONObject reqJson = new JSONObject(sb.toString());
            String action = reqJson.optString("action", "");
            
            DBConnection dbc = new DBConnection();
            
            if("add".equalsIgnoreCase(action)) {
            	// read fields, validate
                String make = reqJson.optString("vehicleMake", "");
                String model = reqJson.optString("vehicleModel", "");
                String year = reqJson.optString("vehicleYear", "");
                String cat = reqJson.optString("vehicleCategory", "");
                String nick = reqJson.optString("nickname", null);

                logger.info("Add vehicle request with parameters vehicle Make:{}, vehicle Model{}, vehicle Year{}, vehicle Category:{} vehicle Nickname:{}", make, model, year, cat, nick);
  			  
                // validate fields, then insert
                int result = dbc.insertVehicleDetail(username, make, model, year, cat, nick);
                if (result == 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Failed to add vehicle\"}");
                    return;
                }
                // fetch updated profile
                logger.info("Vehicle added successfully");
                
                User user = dbc.getUserByUsername(username);
                out.write(buildUserProfileResponse(user).toString());
                return;
                
            } else if ("update".equalsIgnoreCase(action)) {
            	int id = reqJson.optInt("id", -1);
                if (id <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Missing vehicle id\"}");
                    return;
                }
                String make = reqJson.optString("vehicleMake", null);
                String model = reqJson.optString("vehicleModel", null);
                String year = reqJson.optString("vehicleYear", null);
                String cat = reqJson.optString("vehicleCategory", null);
                String nick = reqJson.optString("nickname", null);
                
                logger.info("Update vehicle request with parameters vehicle Make:{}, vehicle Model{}, vehicle Year{}, vehicle Category:{} vehicle Nickname:{}", make, model, year, cat, nick);

                boolean ok = dbc.updateVehicleDetail(id, username, make, model, year, cat, nick);
                if (!ok) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Update failed\"}");
                    return;
                }
                
                logger.info("Vehicle updated successfully");
                
                User user = dbc.getUserByUsername(username);
                out.write(buildUserProfileResponse(user).toString());
                return;
                
            }  else if ("delete".equalsIgnoreCase(action)) {
            	int id = reqJson.optInt("id", -1);
                if (id <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Missing vehicle id\"}");
                    return;
                }

                logger.info("Delete vehicle request with parameters vehicle Id:{}", id);
                
                // ensure user has more than 1 vehicle
                User user = dbc.getUserByUsername(username);
                List<VehicleDetail> vehicles = user.getVehicles();
                if (vehicles == null || vehicles.size() <= 1) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Cannot remove last vehicle\"}");
                    return;
                }

                boolean ok = dbc.deleteVehicle(id, username);
                if (!ok) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Delete failed\"}");
                    return;
                }
                
                logger.info("Vehicle deleted successfully");
                // return updated profile
                user = dbc.getUserByUsername(username);
                out.write(buildUserProfileResponse(user).toString());
                return;
                
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\":\"Unknown action\"}");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Server error\"}");
        }
	}
	
	private JSONObject buildUserProfileResponse(User user) {
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
		
		return json;
	}

}
