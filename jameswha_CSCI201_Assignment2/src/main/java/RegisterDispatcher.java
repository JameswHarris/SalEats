import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Util.*;

/**
 * Servlet implementation class RegisterDispatcher
 */
@WebServlet("/Register")
public class RegisterDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    String sqlInsert = "INSERT INTO Users (email, name, password) VALUES (?,?,?)";
    static final String sqlCheckEmail = "SELECT COUNT(email) as 'result' FROM Users WHERE email = ?";

    /**
     * Default constructor.
     */
    public RegisterDispatcher() {
    }
    
    
    private static boolean checkExisting(String email) {
    	int result = 0;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e);
		}
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement ps = conn.prepareStatement(sqlCheckEmail);) {
			ps.setString(1, email);
			ResultSet res = ps.executeQuery();
			res.next();
			result = res.getInt("result");
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}
    	
    	return (result>0);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String error = "";
        
        //get info from request
        String email = request.getParameter("regEmail");
		String name = request.getParameter("regName");
		String password = request.getParameter("regPassword");
		String confirmPass = request.getParameter("confirmPass");
		String terms = request.getParameter("terms");
		
		
		//validate info
		if (email.contentEquals(""))
			error += "<p>Email Field Empty</p>";
		else if (!Constant.patternMatches(email, Constant.emailPattern)) {
			error += "<p>Invalid Email Entered</p>";
		}
		else if (checkExisting(email)) {
			error += "<p>Account Exists for this Email, Please Sign In</p>";
		}
		if (name.contentEquals(""))
			error += "<p>Name Field Empty</p>";
		else if (!Constant.patternMatches(name, Constant.namePattern)) {
			error += "<p>Invalid Name Entered</p>";
		}
		if (password.contentEquals(""))
			error += "<p>Password Field Empty</p>";
		if (confirmPass.contentEquals(""))
			error += "<p>Confirm Password Field Empty</p>";
		if (!confirmPass.equals(password))
			error += "<p>Passwords did not Match</p>";
		if (terms == null)
			error += "<p>Must Agree to the Terms and Conditions</p>";
		
	
		if (error.equals("")) {
			//send info to SQL using JDBC
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}catch(Exception e) {
				System.out.println(e);
			}
			try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
				PreparedStatement ps = conn.prepareStatement(sqlInsert);) {
				ps.setString(1, email);
				ps.setString(2, name);
				ps.setString(3, password);
				ps.execute();
			} catch (SQLException sqle) {
				System.out.println ("SQLException: " + sqle.getMessage());
			}
			Cookie Username = new Cookie("name", URLEncoder.encode(name, "UTF-8"));
			Username.setMaxAge(60*60);
			response.addCookie(Username);
			
			response.sendRedirect("index.jsp");
		}
		else
		{
			request.setAttribute("error", error);
			request.getRequestDispatcher("auth.jsp").include(request, response);
		}

    	
		
		
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
