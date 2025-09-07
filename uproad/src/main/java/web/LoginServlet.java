package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.User;
import dbc.DBConnection;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
        String password = request.getParameter("password");

        DBConnection dbc = new DBConnection();
        User loggedInUser = dbc.isValidUser(username, password);
        
        if (loggedInUser != null) {
        	// Set session attribute to indicate that the user is logged in
            HttpSession session = request.getSession();
            session.setAttribute("userLoggedIn", true);
            session.setAttribute("user", loggedInUser);
            
            if(loggedInUser.getAdmin()==1) 
            	response.sendRedirect("welcome.jsp");
            else 
            	response.sendRedirect("userProfile.jsp");
            
        } else {
            // Set an error message and forward back to the login page
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
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
