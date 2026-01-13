package web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.DBConnection;
import exc.DuplicateGarageException;

/**
 * Servlet implementation class InsertDataServlet
 */
@WebServlet("/InsertDataServlet")
public class InsertDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String garageName = request.getParameter("garageName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        String contactNumber2 = request.getParameter("contactNumber2");
        String contactNumber3 = request.getParameter("contactNumber3");
        String nearestCity = request.getParameter("nearestCity");
        String highway = request.getParameter("highway");
        String website = request.getParameter("website");
        String field12 = request.getParameter("field12");
        String field13 = request.getParameter("field13");
        
        // Retrieving selected checkboxes
        String[] vehicleCategories = request.getParameterValues("vehicleCategory[]");
        String[] servicesProvided = request.getParameterValues("servicesProvided[]");
     
        // Convert arrays to strings for storage or display
        String vehicleCategoryStr = (vehicleCategories != null) ? String.join(", ", vehicleCategories) : "None";
        String servicesProvidedStr = (servicesProvided != null) ? String.join(", ", servicesProvided) : "None";


        DBConnection dbc = new DBConnection();
        try {
        	dbc.insertGarageData(garageName, address, contactNumber, contactNumber2, contactNumber3, nearestCity, highway, website, vehicleCategoryStr, servicesProvidedStr, field12, field13, null, null);
        	request.setAttribute("message", "Garage information successfully inserted.");
        	
        } catch (DuplicateGarageException e) {
        	request.setAttribute("message", "Failed to insert data: " + e.getMessage());
        	
		} catch (SQLException e) {
        	request.setAttribute("message", "Failed to insert data: " + e.getMessage());
		}
        
        request.getRequestDispatcher("insertData.jsp").forward(request, response);
       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
