package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Garage;
import dbc.DBConnection;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve user and vehicle details from request
        String username = request.getParameter("username");
        String selectedVehicle = request.getParameter("selectedVehicle");
        String[] serviceTypes = request.getParameterValues("serviceType");
        //String serviceType = request.getParameter("serviceType");
        String nearestCity = request.getParameter("nearestCity");

        // Split selected vehicle details
        String[] vehicleParts = selectedVehicle.split(",");
        
        String vehicleCategory = vehicleParts.length > 3 ? vehicleParts[3] : "";

        List<Garage> garageList = new ArrayList<>();
        
        garageList = new DBConnection().getGaragebyServiceAndcity(username, vehicleCategory, serviceTypes, nearestCity);
        
        request.setAttribute("garages", garageList);
        request.getRequestDispatcher("searchResult.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
