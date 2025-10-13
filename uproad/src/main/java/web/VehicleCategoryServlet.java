package web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.VehicleCategory;
import dbc.DBConnection;

/**
 * Servlet implementation class VehicleCategoryServlet
 */
@WebServlet("/VehicleCategoryServlet")
public class VehicleCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VehicleCategoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ArrayList<VehicleCategory> catList = new DBConnection().getVehicleCategory();
        ArrayList<String> catString = new ArrayList<String>();
        
        for(VehicleCategory cat : catList) {
        	catString.add(cat.getVehicleCategory());
        }
        
        String json = new Gson().toJson(catString);
        System.out.println(json);
        response.getWriter().write(json);
       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
