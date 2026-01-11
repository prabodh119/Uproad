package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.Garage;
import dao.User;
import dbc.DBConnection;
import exc.DuplicateGarageException;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/AdminSearchServlet")
public class AdminSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Logger logger = LoggerFactory.getLogger(AdminSearchServlet.class);
		
		String searchAll = request.getParameter("searchAll"); 
		String searchType = request.getParameter("searchType");
		String searchCriteria = request.getParameter("searchCriteria");
		String action = request.getParameter("action");
		
		logger.info("{} - {}, {}, {}, {}", ((User) request.getSession().getAttribute("user")).getUsername(), searchAll, searchType, searchCriteria, action);
		//logger.info(searchAll+ ","+searchType+ ","+searchCriteria +","+action);
		
		if(action!= null && action.equals("edit")) {
			logger.info("edit request");
			
			int index = Integer.parseInt(request.getParameter("index"));
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String contact = request.getParameter("contact");
			String contact2 = request.getParameter("contact2");
			String contact3 = request.getParameter("contact3");
			String city = request.getParameter("city");
			String highway = request.getParameter("highway");
			String website =  request.getParameter("website");
			
			// Get selected checkboxes as array
	        String[] vehicleCategories = request.getParameterValues("vehicleCategory[]");
	        String[] servicesProvided = request.getParameterValues("servicesProvided[]");
	       
			String field12 = request.getParameter("field12");
			String field13 = request.getParameter("field13");
			
			// Convert arrays to comma-separated strings
	        String vehicleCategoryStr = (vehicleCategories != null) ? String.join(", ", vehicleCategories) : "None";
	        String servicesProvidedStr = (servicesProvided != null) ? String.join(", ", servicesProvided) : "None";
			
	        DBConnection dbc = new DBConnection();
	        try {
	        	dbc.editRecord(index, name, address, contact, contact2, contact3, city, highway, website, vehicleCategoryStr, servicesProvidedStr, field12, field13, null, null);
	        	request.setAttribute("message", "Data updated successfully!");
	        	
	        } catch (DuplicateGarageException e) {
	        	logger.info("Failed to insert data: " + e.getMessage());
	        	request.setAttribute("message", "Failed to update data: " + e.getMessage());
	        	
	        } catch (SQLException e) {
	        	logger.info("Failed to insert data: " + e.getMessage());
	        	request.setAttribute("message", "Failed to update data: " + e.getMessage());
	        }
					
		} else if (action!= null && action.equals("delete")) {
			logger.info("delete request");
		
			int id=Integer.parseInt(request.getParameter("index"));
			
			DBConnection dbc = new DBConnection();
			try {
			    boolean deleted = dbc.deleteRecord(id);
			    if (deleted) {
			    	logger.info("Garage deleted successfully!");
			        request.setAttribute("message", "Record deleted!");
			    } else {
			    	logger.info("Garage with index " + id + " not found.");
			        request.setAttribute("message", "Delete failed! Record not found.");
			    }
			} catch (SQLException e) {
				logger.info("Database error: " + e.getMessage());
			    request.setAttribute("message", "Database error: " + e.getMessage());
			}

		}
		
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		
		if(("name").equals(searchType)) {
			garageList= new DBConnection().getGarageByName(searchCriteria);
			request.setAttribute("type", searchType);
			request.setAttribute("criteria", searchCriteria);
		
		} else if(("city").equals(searchType)) {
			garageList= new DBConnection().getGarageByCity(searchCriteria);
			request.setAttribute("type", searchType);
			request.setAttribute("criteria", searchCriteria);
		
		} else if(("highway").equals(searchType)) {
			garageList= new DBConnection().getGarageByHighway(searchCriteria);
			request.setAttribute("type", searchType);
			request.setAttribute("criteria", searchCriteria);
		
		} else if (("phone").equals(searchType)) {
			garageList= new DBConnection().getGarageByPhone(searchCriteria);
			request.setAttribute("type", searchType);
			request.setAttribute("criteria", searchCriteria);
		} else 
			garageList= new DBConnection().getAllGarage();
		
		List<String[]> results = new ArrayList<>();
		for (Garage garage : garageList) {
			results.add(new String[]{
					String.valueOf(garage.getIndex()), 
					nullCheckCorrect(garage.getName()), 
					nullCheckCorrect(garage.getAddress()), 
					nullCheckCorrect(garage.getField13()),
					nullCheckCorrect(garage.getContactNo()), 
					nullCheckCorrect(garage.getContactNo2()), 
					nullCheckCorrect(garage.getContactNo3()),
					nullCheckCorrect(garage.getNearCity()), 
					nullCheckCorrect(garage.getRoad()),
					nullCheckCorrect(garage.getWebsite()),
					nullCheckCorrect(garage.getVehicle_category()),
					nullCheckCorrect(garage.getServices()),
					nullCheckCorrect(garage.getField12()),
					String.valueOf(garage.getAvg_rating())
			});
		}
		
		request.setAttribute("results", results);
        request.getRequestDispatcher("adminSearchResults.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String nullCheckCorrect(String value) {
	    return value == null ? "" : value;
	}

}
