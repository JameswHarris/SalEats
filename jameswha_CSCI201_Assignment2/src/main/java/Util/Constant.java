package Util;

import java.util.regex.Pattern;

public class Constant {
    static public String DBUserName = "root";
    static public String DBPassword = "root";
    static public String FileName = "/WEB-INF/restaurant_data.json";
    static public final String url = "jdbc:mysql://localhost:3306/PA2";


    static public Pattern namePattern = Pattern.compile("^[ A-Za-z]+$");
    static public Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."
            + "[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
            + "A-Z]{2,7}$");
    
    public static boolean patternMatches(String value, Pattern pat) {
        return pat.matcher(value).matches();
    }
}
