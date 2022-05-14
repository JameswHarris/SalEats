import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Util.Business;
import Util.Constant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Servlet implementation class SearchDispatcher
 */
@WebServlet("/DetailsDispatcher")
public class DetailsDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static String sqlGetObjects = "SELECT * FROM Restaurant AS rest INNER JOIN Rating_details AS ratDet ON rest.rating_id = ratDet.rating_id INNER JOIN Restaurant_details AS restDet ON rest.details_id = restDet.details_id"
    		+ " INNER JOIN Category AS cat ON rest.restaurant_id = cat.restaurant_id WHERE rest.restaurant_id = ?";

    /**
     * Default constructor.
     */
    public DetailsDispatcher() {
    }
    
    /**
     * @param keyWord    the search keyword
     * @param sort       the sort option (price, review count, rating)
     * @param searchType search in category or name
     * @return the list of business matching the criteria
     */
   
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String id = request.getParameter("id");
		ArrayList<Business> result = new ArrayList<Business>();

    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e);
		}
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement ps = conn.prepareStatement(sqlGetObjects);) {
			ps.setString(1, id);
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				boolean exists = false;
				String resID = res.getString("restaurant_id");
				for(Business bus: result) {
					if(bus.getID().equals(resID)) { 
						exists = true;
						bus.addCategory(res.getString("category_name"));
						break;
					}
				}
				if(!exists) {
					ArrayList<String> cats = new ArrayList<String>();
					cats.add(res.getString("category_name"));
					result.add(new Business(resID, res.getString("restaurant_name"), res.getString("images_url"), res.getDouble("rating"), res.getString("address"), cats, res.getString("estimated_price"), res.getString("phone_no"), res.getString("yelp_url"), res.getInt("review_count")));
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
    	
		request.setAttribute("data", result);
		request.getRequestDispatcher("details.jsp").forward(request, response);


    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}