package web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.DBConnection;
import util.Config;
import util.EmailService;

/**
 * Servlet implementation class ForgotPasswordServlet
 */
@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgotPasswordServlet() {
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
		String email = request.getParameter("email");
        DBConnection dbc = new DBConnection();

        String token = UUID.randomUUID().toString();
        String expiry = LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (dbc.saveResetToken(email, token, expiry)) {
        	
            String resetLink = Config.getBaseUrl()+ "/resetPassword.jsp?token=" + token;
            
            Boolean emailSuccess = EmailService.sendPasswordResetEmail(email, resetLink);
            
            if (emailSuccess)
            	request.setAttribute("message", "Password reset link sent to you email");
            else
            	request.setAttribute("message", "Could not send the reset email !!");
            
        } else {
        	request.setAttribute("error", "Email not found in records !!");
            
        }
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
	}

}
