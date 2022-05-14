import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Util.Business;
import Util.Constant;
import Util.ParseHelper;

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
@WebServlet("/Search")
public class SearchDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static Boolean ready = false;
    static final String sqlResDetailInsert = "INSERT INTO Restaurant_details(images_url, address, phone_no, estimated_price, yelp_url) VALUES (?,?,?,?,?)";
    static final String sqlRatDetailInsert = "INSERT INTO Rating_details(review_count, rating) VALUES (?,?)";
    static final String sqlRestInsert = "INSERT INTO Restaurant(restaurant_id, restaurant_name, details_id, rating_id) VALUES (?,?,?,?)";
    static final String sqlCatInsert = "INSERT INTO Category(category_name, restaurant_id) VALUES (?,?)";


    /**
     * Default constructor.
     */
    public SearchDispatcher() {
    }
    
    private boolean insertToDB(ArrayList<Business> restaurants) {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e);
		}
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement resDetSt = conn.prepareStatement(sqlResDetailInsert, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement ratDetSt = conn.prepareStatement(sqlRatDetailInsert, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement restSt = conn.prepareStatement(sqlRestInsert, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement catSt = conn.prepareStatement(sqlCatInsert, Statement.RETURN_GENERATED_KEYS);) {
			
			for(Business rest : restaurants) {
	    		int resDetFK = 0, ratDetFK = 0;
	    		
	    		//insert into restaurant_details, save FK
				resDetSt.setString(1, rest.getimage_url());
				resDetSt.setString(2, rest.getLocation());
				resDetSt.setString(3, rest.getPhone());
				resDetSt.setString(4, rest.getPrice());
				resDetSt.setString(5, rest.getUrl());
				resDetSt.executeUpdate();
				
				ResultSet resDetrs = resDetSt.getGeneratedKeys();
			    resDetrs.next();
			    resDetFK = resDetrs.getInt(1);

	    		//insert into rating_details, save FK
			    ratDetSt.setInt(1, rest.getReviewCount());
				ratDetSt.setDouble(2, rest.getRating());
				ratDetSt.executeUpdate();
				
				ResultSet ratDetrs = ratDetSt.getGeneratedKeys();
			    ratDetrs.next();
			    ratDetFK = ratDetrs.getInt(1);
			    
	    		//insert into restaurant
			    restSt.setString(1, rest.getID());
			    restSt.setString(2, rest.getName());
			    restSt.setInt(3, resDetFK);
				restSt.setInt(4, ratDetFK);
				restSt.executeUpdate();
			    
			    
	    		//insert into category
				for(String s: rest.getCategories()) {
					catSt.setString(1, s);
					catSt.setString(2, rest.getID());
					catSt.executeUpdate();
				}
	    	}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
    	
    	
    	return true;
    }
    

    /**
     * @param keyWord    the search keyword
     * @param sort       the sort option (price, review count, rating)
     * @param searchType search in category or name
     * @return the list of business matching the criteria
     */
    public static ArrayList<Business> performSearch(String keyWord, String sort, String searchType) {
    	ArrayList<Business> result = new ArrayList<Business>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);) {
        	String sqlSearchCat = "SELECT * FROM Restaurant AS rest\n"
            		+ "INNER JOIN Rating_details AS ratDet ON rest.rating_id = ratDet.rating_id\n"
            		+ "INNER JOIN Restaurant_details AS restDet ON rest.details_id = restDet.details_id\n"
            		+ "INNER JOIN Category AS cat ON rest.restaurant_id = cat.restaurant_id\n"
            		+ "WHERE cat.category_name LIKE ?"
            		+ "ORDER BY ";
        	String sqlSearchName = "SELECT * FROM Restaurant AS rest\n"
            		+ "INNER JOIN Rating_details AS ratDet ON rest.rating_id = ratDet.rating_id\n"
            		+ "INNER JOIN Restaurant_details AS restDet ON rest.details_id = restDet.details_id\n"
            		+ "INNER JOIN Category AS cat ON rest.restaurant_id = cat.restaurant_id\n"
            		+ "WHERE cat.category_name LIKE ?"
            		+ "ORDER BY ";
        	if(sort.equals("price")) {
            	sqlSearchCat += "estimated_price;";
            	sqlSearchName += "estimated_price;";

        	}
        	else if(sort.equals("review-count")) {
        		sqlSearchCat += "review_count DESC;";
        		sqlSearchName += "review_count DESC;";

        	}
        	else {
        		sqlSearchCat += "rating DESC;";
        		sqlSearchName += "rating DESC;";

        	}
        	
        	PreparedStatement ps = null;
        	if(searchType.equals("Name")) {
            	ps = conn.prepareStatement(sqlSearchName);

        	}
        	else {
            	ps = conn.prepareStatement(sqlSearchCat);
        	}
        	ps.setString(1, "%"+keyWord+"%");
        	
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
        
        return result;

    }
    
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS 'data' FROM Restaurant");
			ResultSet res = ps.executeQuery();
			res.next();
			ready = res.getInt("data")!=0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        if (ready) {
            return;
        }
        ready = true;
        
        //get businessHelper array from json
        ArrayList<Business> restaurantList = new ArrayList<Business>();
        Gson gson = new Gson();
        String path = this.getServletContext().getRealPath(Constant.FileName);
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			ParseHelper response = gson.fromJson(reader, ParseHelper.class);
			restaurantList = response.getParseHelper();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e) {
			System.exit(0);
		}
        //iterate the businessHelper array and insert every business into the DB
		insertToDB(restaurantList);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String choice = request.getParameter("choice");
		String searchValue = request.getParameter("searchValue");
		String radioGroup = request.getParameter("radioGroup");
		
		request.setAttribute("searchTitle", "Results for " + searchValue + " in " + choice);
		request.setAttribute("data", performSearch(searchValue, radioGroup, choice));
		request.getRequestDispatcher("search.jsp").forward(request, response);

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