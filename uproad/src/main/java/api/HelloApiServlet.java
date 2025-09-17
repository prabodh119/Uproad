package api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import util.TokenUtil;

/**
 * Servlet implementation class HelloApiServlet
 */
@WebServlet("/api/hello")
public class HelloApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloApiServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Authorization");
        response.setContentType("application/json");
        
        JSONObject json = new JSONObject();

        if (token != null && token.startsWith("Bearer ") && TokenUtil.isValid(token.substring(7))) {
            String username = TokenUtil.validateToken(token.substring(7));
            json.put("message", "Hello, " + username + "!");
            
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.put("message", "Unauthorized");
        }

        response.getWriter().write(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
