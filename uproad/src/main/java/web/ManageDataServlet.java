package web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.DBConnection;

/**
 * Servlet implementation class ManageDataServlet
 */
@WebServlet("/ManageDataServlet")
public class ManageDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		int id = Integer.parseInt(request.getParameter("id"));
		
		if(action.equals("delete")) {
			DBConnection dbc = new DBConnection();
			try {
			    boolean deleted = dbc.deleteRecord(id);
			    if (deleted) {
			        System.out.println("Garage deleted successfully!");
			        request.setAttribute("message", "Record deleted!");
			    } else {
			        System.out.println("Garage with index " + id + " not found.");
			        request.setAttribute("message", "Delete failed! Record not found.");
			    }
			} catch (SQLException e) {
			    System.out.println("Database error: " + e.getMessage());
			    request.setAttribute("message", "Database error: " + e.getMessage());
			}
	        
	        request.getRequestDispatcher("searchResults.jsp").forward(request, response);
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
