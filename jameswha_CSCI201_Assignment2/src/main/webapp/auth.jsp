<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="301645777112-2rlc9gth0f5d4reimjcm9bf0kj7ahec0.apps.googleusercontent.com"
          name="google-signin-client_id">
    <title>Login / Register</title>
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link
            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
    <script crossorigin="anonymous"
            src="https://kit.fontawesome.com/3204349982.js"></script>
    <script async defer src="https://apis.google.com/js/platform.js"></script>
    <link href="index.css" type="text/css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto"
          rel="stylesheet" type="text/css">
    <script src="https://apis.google.com/js/api:client.js"></script>
    <%@ page import="java.net.URLDecoder" %>

</head>
<body>
	<header>
		<div style="background-color:#ff5050; color:white; text-align:center">
			<% String er = (String) request.getAttribute("error");
			if (er != null) out.println(er);
			%>
		</div>
	</header>
	<div class="topnav">
       		<a href="index.jsp"><span style="color:#cc0000; font-family:Lobster">SalEats!</span></a>
       		<a href="auth.jsp" style = "font-size: 15px;">
			<%	

	       			Cookie cookie = null;
			        Cookie[] cookies = null;
					
			        // Get an array of Cookies associated with the this domain
			        cookies = request.getCookies();
					String name = null;
			        if( cookies != null ) {
			           for (int i = 0; i < cookies.length; i++) {
			              cookie = cookies[i];
			              name = URLDecoder.decode(cookie.getValue(), "UTF-8");
			              if(cookie.getName().equals("name")){
			              out.print("Hi " + name + "!");
			              break;
			              }
			           }
			        }
		      %>
			</a>
			<% 
	       			boolean loggedIn = false;
					if(name != null){
						if(cookie.getName().equals("name")){
				              loggedIn = true;
		       			}
					}
       		%>
			<a href="LogoutDispatcher"><span style="float:right; transform: translateX(-20px)"><%if(loggedIn) out.print("Logout"); %></span></a>
       		<a href="auth.jsp"><span style="float:right; transform: translateX(-20px)"><%if(!loggedIn) out.print("Login/Register"); %></span></a>
       		<a href="index.jsp"><span style="float:right; transform: translateX(-40px)">Home</span></a>
       		
    </div>
    
    <div>
    	<div style="float:left; width:50%">
    		<div style="float:left; width:25%; height:500px"></div>
	    	<% 
	    	String logEmail = (String) request.getParameter("logEmail");
			
			if (logEmail == null) logEmail = "";
			%>	
	    		
	    		<h1 style="margin-top: 70px"> Login </h1>
	    		
	    		<form action = "Login" method = "GET">
		    		<p style="margin-left: 5px">Email</p>
		    		<input type ="text" name="logEmail" style ="margin-left: 5px; width: 60%" value = <%=logEmail%>>
		    		
		    		<p style="margin-left: 5px">Password</p>
		    		<input type ="password" name="logPassword" style ="margin-left: 5px; width: 60%"> <br>
	    		
	    			<button type="submit" class="sign-in"><i class= "fa fa-sign-in" aria-hidden="true"></i> Sign In</button>
	    		</form>
	    		
	    		
	    		<button type="submit" class="google-sign"><i class="fa fa-google"> Sign in with Google</i></button>
	    		
	    		
    		
    	</div>
    	
    	<% 
    	String regEmail = (String) request.getParameter("regEmail");
		String regName = (String) request.getParameter("regName");
		String regPassword = (String) request.getParameter("regPassword");
		String confirmPass = (String) request.getParameter("confirmPass");
    	if (regPassword == null) regPassword = "";
		if (regName == null) regName = "";
		if (regEmail == null) regEmail = "";
		if (confirmPass == null) confirmPass = "";
		%>
    	<div style="float:right; width:50%">
    		<h1 style="margin-left: 5px; margin-top: 10px"> Register </h1>
    		
    		<form action = "Register" method = "GET">
	    		<p style="margin-left: 5px">Email</p>
	    		<input type ="text" name="regEmail" style ="margin-left: 5px; width:60%" value = <%=regEmail%>>
	    		
	    		<p style="margin-left: 5px">Name</p>
	    		<input type ="text" name="regName" style ="margin-left: 5px; width:60%"value = <%=regName%>>
	    		
	    		<p style="margin-left: 5px">Password</p>
	    		<input type ="password" name="regPassword" style ="margin-left: 5px; width:60%"value = <%=regPassword%>>
	    		
	    		<p style="margin-left: 5px">Confirm Password</p>
	    		<input type ="password" name="confirmPass" style ="margin-left: 5px; width:60%; margin-bottom:5px"value = <%=confirmPass%>> <br>
	    		
	    		<input type="checkbox" name="terms">
	    		<label for="terms">I have read and Agree to the Terms and Conditions</label>
	    		
	    		<button type="submit" value="true" class="sign-in"><i class="fa fa-user-plus"> Create Account</i></button>
    		</form>
    		
    	</div>
    </div>
    
</body>
</html>