package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

import dao.User;
import dbc.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ApiLoginServlet
 */
@WebServlet("/ApiLoginServlet")
public class ApiLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Read JSON body from Android request
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject input = new JSONObject(sb.toString());
            
            String username = input.getString("username");
            String password = input.getString("password");

            // Validate against DB
            DBConnection dbc = new DBConnection();
            User loggedInUser = dbc.isValidUser(username, password);

            JSONObject result = new JSONObject();
            if (loggedInUser != null) {
                result.put("success", true);
                result.put("message", "Login OK");
                result.put("username", loggedInUser.getUsername());
                result.put("admin", loggedInUser.getAdmin());
            } else {
                result.put("success", false);
                result.put("message", "Invalid username or password");
            }

            out.print(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("success", false);
            error.put("message", "Server error");
            out.print(error.toString());
        } finally {
            out.flush();
            out.close();
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
