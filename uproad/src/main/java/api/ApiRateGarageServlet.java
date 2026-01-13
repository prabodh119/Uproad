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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbc.DBConnection;
import util.TokenUtil;

/**
 * Servlet implementation class RateGarageServlet
 */
@WebServlet("/api/rateGarage")
public class ApiRateGarageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiRateGarageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        Logger logger = LoggerFactory.getLogger(ApiRateGarageServlet.class);
        
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
        	
            // Parse incoming parameters
            int garageId = reqJson.getInt("garageId");
            String userId = reqJson.getString("userId");
            int criteria1 = reqJson.getInt("serviceQuality");
            int criteria2 = reqJson.getInt("priceFairness");
            int criteria3 = reqJson.getInt("staffBehavior");
            int criteria4 = reqJson.getInt("timeEfficiency");
            int criteria5 = reqJson.getInt("facilityCleanliness");
            String serviceDetails = reqJson.getString("serviceDetails");
            String serviceDate = reqJson.getString("serviceDate");
            
            logger.info("Garage rating request with parameters garage id:{}, user id{}, ratings:{},{},{},{},{}, service details:{}, saervice data:{}", garageId, userId, criteria1, criteria2, criteria3, criteria4, criteria5, serviceDetails, serviceDate);

            DBConnection dbc = new DBConnection();

            boolean success = dbc.updateGarageRating(garageId, userId, criteria1, criteria2, criteria3, criteria4, criteria5, serviceDetails, serviceDate);

            JSONObject json = new JSONObject();
            if (success) {
            	float updatedRating = dbc.getGarageById(garageId).getAvg_rating();
            	
                json.put("status", "success");
                json.put("garageId", garageId);
                json.put("avg_rating", updatedRating);
                
                logger.info("Garage rating successful. Updated rating:{}", updatedRating);
            } else {
                json.put("status", "error");
                logger.error("Garage rating error!!");
            }

            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        } 
	}

}
