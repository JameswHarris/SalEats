package Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserHelper {
    /**
     * check if name is valid
     *
     * @param name the name user provides
     * @return valid or not valid
     */
    public static boolean validName(String name) {
        return Constant.namePattern.matcher(name).matches();
    }

    /**
     * check if email is valid
     *
     * @param email the email user provides
     * @return valid or not valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return Constant.emailPattern.matcher(email).matches();
    }

    /**
     * Get username with the email
     *
     * @param email
     * @return userName
     * @throws SQLException
     */
    public static String getUserName(String email) {
    	String result = null;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e);
		}
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT name FROM Users WHERE email = ?");) {
			ps.setString(1, email);
			ResultSet res = ps.executeQuery();
			res.next();
			result = res.getString("name");
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}
    	
    	return result;
    }

 
    /**
     * check if the email and password matches
     *
     * @param email
     * @param password
     */
    public static boolean checkPassword(String email, String password) {
    	String result = null;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e);
		}
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT password FROM Users WHERE email = ?");) {
			ps.setString(1, email);
			ResultSet res = ps.executeQuery();
			res.next();
			result = res.getString("password");
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}
    	
    	return (result != null && result.equals(password));
    }

    /**
     * Check if email is already registered
     *
     * @param email
     * @param request
     * @param response
     * @return email registered or not
     * @throws ServletException
     * @throws IOException
     */
    public static boolean checkExisting(String email) {
    	int result = 0;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e);
		}
		try (Connection conn = DriverManager.getConnection(Constant.url,Constant.DBUserName, Constant.DBPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT COUNT(email) as 'result' FROM Users WHERE email = ?");) {
			ps.setString(1, email);
			ResultSet res = ps.executeQuery();
			res.next();
			result = res.getInt("result");
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}
    	
    	return (result>0);
    }
}
