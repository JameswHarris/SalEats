<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Details</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
    <script src="https://kit.fontawesome.com/3204349982.js"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="index.css">
    <%@ page import="java.net.URLDecoder" %>
   	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>     
    
    
</head>
<%
    //TODO perform search

    //TODO check if logged in
%>

<body>
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
   		<div style="margin-bottom: 40px; padding: 10px">
    		<form action = "Search" method = "GET">
        
		        <select name="choice" style="margin-left:3%">
		        	<option value="Category" selected>Category</option>
		        	<option value="Name">Name</option>
		        </select>
		        
		        <input name = "searchValue" type ="text" placeholder="Search.." style ="width:50%">
		        <button type="submit" class="search-button"><i class = "fa fa-search" style="color:white"></i></button>
		        
		        <input type="radio" id="price" value="price" name="radioGroup">
		        <label for="price">Price</label>
		        
		        <input type="radio" id="review-count" value= "review-count" name="radioGroup">
		        <label for="review-count">Review Count</label>
		        
		        <input type= "radio" id="rating" value = "rating" name="radioGroup">
		        <label for="rating">Rating</label>
	      	</form>
    	</div>
    	<c:forEach items="${data}" var="element">  
		  	<div class="media" style = "border-bottom:1px solid silver">
				<div class="media-left">
					<a href="DetailsDispatcher?id="${element.getID()}> <img style="object-fit:cover; border-radius:25px; width: 200px; height: 200px; margin: 5px 60px 10px 100px;" src = "${element.getimage_url()}" /> </a> </br>
				</div>
				<div class="media-body" style="margin-top:10px">
					<a href="DetailsDispatcher?id=<c:out value="${element.getID()}"/>" style="font-size:18px; text-decoration:underline"> <c:out value="${element.getName()}" /> <br> </a>
					<p> Address: ${element.getLocation()} </p>
					<p> ${element.getPhone()} </p>
					<p> Categories: ${element.turnCategoriesIntoList()} </p>
					<p> Price: ${element.getPrice()} </p>
					<p> Rating: ${element.getRating()} </p>
				</div>
			</div>
	  
		</c:forEach> 
</body>
</html>