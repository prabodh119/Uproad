package api;

import dbc.DBConnection;
import dao.VehicleCategory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class ApiVehicleCategoriesServlet
 */
@WebServlet("/api/vehicleCategories")
public class ApiVehicleCategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiVehicleCategoriesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
			/*
			 * String authHeader = request.getHeader("Authorization"); if (authHeader ==
			 * null || !authHeader.startsWith("Bearer ")) {
			 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			 * out.write("{\"error\":\"Missing or invalid Authorization header\"}"); return;
			 * }
			 * 
			 * String token = authHeader.substring(7); String username =
			 * TokenUtil.validateToken(token); if (username == null) {
			 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			 * out.write("{\"error\":\"Invalid or expired token\"}"); return; }
			 */

            DBConnection dbc = new DBConnection();
            List<VehicleCategory> categories = dbc.getVehicleCategory(); // implement in DBConnection

            JSONArray arr = new JSONArray();
            for (VehicleCategory vc : categories) {
                JSONObject obj = new JSONObject();
                obj.put("vehicleCategory", vc.getVehicleCategory());
                arr.put(obj);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(arr.toString());
            System.out.println(arr.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
            	out.write("{\"error\":\"Server error\"}");
            }
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
