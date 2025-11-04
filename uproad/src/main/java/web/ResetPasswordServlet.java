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
 * Servlet implementation class ResetPasswordServlet
 */
@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetPasswordServlet() {
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
		String token = request.getParameter("token");
        String password = request.getParameter("password");
        
        DBConnection dbc = new DBConnection();
        
        if (dbc.isValidResetToken(token)) {
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            
            if (dbc.updatePasswordWithToken(token, hashed))
            	request.setAttribute("message", "Password reset successfull");
            else
            	request.setAttribute("error", "Could not update password");
        } else {
        	request.setAttribute("error", "Invalid or expired token");
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
	}

}
