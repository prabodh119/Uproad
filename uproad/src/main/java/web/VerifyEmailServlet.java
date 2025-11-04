package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbc.DBConnection;

/**
 * Servlet implementation class VerifyEmailServlet
 */
@WebServlet("/verifyEmail")
public class VerifyEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyEmailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Logger logger = LoggerFactory.getLogger(VerifyEmailServlet.class);
		
		String token = request.getParameter("token");
		DBConnection dbc = new DBConnection();
		
		logger.info("email verification request from web user with token : "+token);
		
		if(token != null && dbc.verifyUser(token)) {
			request.setAttribute("message", "Email verified successfully Please Login to continue.");
		} else
			request.setAttribute("error", "Invalid or expired verification link");
		
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
