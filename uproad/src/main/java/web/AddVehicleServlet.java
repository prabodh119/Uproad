package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.User;
import dbc.DBConnection;

/**
 * Servlet implementation class EditProfileServlet
 */
@WebServlet("/EditProfileServlet")
public class AddVehicleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddVehicleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve vehicle data from form submission
        String vehicleMake = request.getParameter("vehicleMake");
        String vehicleModel = request.getParameter("vehicleModel");
        String vehicleYear = request.getParameter("vehicleYear");
        String vehicleCategory = request.getParameter("vehicleCategory");
        String vehicleNickname = request.getParameter("vehicleNickname");// New dropdown selection
        
        User loggedInUser = (User) request.getSession().getAttribute("user");
        String username = loggedInUser.getUsername();
        
        int result = new DBConnection().insertVehicleDetail(username, vehicleMake, vehicleModel, vehicleYear, vehicleCategory, vehicleNickname);
        
        if(result > 0) {
        	request.setAttribute("message", "Vehicle Details successfully inserted.");
        	User loggInUser = new DBConnection().getUserByUsername(username);
        	request.getSession().setAttribute("user", loggInUser);
        	response.sendRedirect("userProfile.jsp");
        	
        } else
        	request.setAttribute("message", "Failed to insert data.");    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
