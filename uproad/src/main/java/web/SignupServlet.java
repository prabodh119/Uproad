package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import dbc.DBConnection;

/**
 * Servlet implementation class SignupServlet
 */
@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
        String telephone = request.getParameter("telephone");
        String idNumber = request.getParameter("idNumber"); // Used as username
        String password = request.getParameter("password"); // Get password input
        String vehicleMake = request.getParameter("vehicleMake");
        String vehicleModel = request.getParameter("vehicleModel");
        String vehicleYear = request.getParameter("vehicleYear");
        String vehicleCategory = request.getParameter("vehicleCategory");
        
        // Encrypt the password before saving it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Here, insert the data (including hashedPassword) into the database
        DBConnection dbc = new DBConnection();
        int result = dbc.insertUser(idNumber, hashedPassword, telephone, name, vehicleMake, vehicleModel, vehicleYear, vehicleCategory);
        
        if(result > 0)
        	request.setAttribute("message", "Successfully Registered. Please Login.");
        else
        	request.setAttribute("message", "Registration Failed !");
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
