<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            text-align: center;
        }
        .menu-bar {
            background-color: #333;
            overflow: hidden;
        }
        .menu-bar a {
            float: left;
            display: block;
            color: white;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
        }
        .menu-bar a:hover {
            background-color: #45a049;
        }      
        .section {
            margin: 20px 0;
        }
        .search-box {
        	margin: 10px;
            padding: 15px;
            background-color: #ffffff;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .search-box input[type="text"] {
            width: 80%;
            padding: 10px;
            margin-right: 10px;
        }
        .search-box button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        .search-box button:hover {
            background-color: #45a049;
        }
        .back-button {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            margin-top: 20px;
        }
        .back-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Welcome Message Section -->
        <div class="header">
            <h1>Welcome, ${user.getName()}!</h1>   
        </div>
        
        <!-- Menu Bar -->
        <div class="menu-bar">
            <a href="insertData.jsp">Insert Garage Data</a>
            <!-- You can add more links here as needed -->
            <a href="/AdminSearchServlet?searchAll=true">Show All</a>
            <a href="#">Yet Another Link</a>
        </div>

        <!-- Search Box Section -->
        <div class="section">
            <h2>Search Garage</h2>
	            <div class="search-box">
		            <form id="searchForm" action="/AdminSearchServlet" method="post">
		            	<input type="hidden" name="searchType" id="searchType" value="">
		            	<input type="hidden" name="searchCriteria" id="searchCriteria" value="">
			            <!-- Name -->
			            <label for="nameInput">Search by Name:</label><br>
			            <input type="text" id="nameInput" name="name" placeholder="Enter garage name...">
			            <button type="button" onclick="submitSearchForm('name')">Search</button><br><br>
			
			            <!-- City -->
			            <label for="cityInput">Search by City:</label><br>
			            <input type="text"  id="nearestCity" name="city" placeholder="Enter city...">
			            <button type="button" onclick="submitSearchForm('city')">Search</button><br>
						<div id="citySuggestions"></div>
						<br>
						
			            <!-- Highway -->
			            <label for="highwayInput">Search by Highway:</label><br>
			            <input type="text"  id="highway" name="highway" placeholder="Enter highway...">
			            <button type="button" onclick="submitSearchForm('highway')">Search</button><br>
			            <div id="highwaySuggestions"></div>
			            <br>
			            
			            <!-- Telephone -->
			            <label for="phoneInput">Search by Phone No.:</label><br>
			            <input type="text"  id="phone" name="phone" placeholder="Enter phone no....">
			            <button type="button" onclick="submitSearchForm('phone')">Search</button><br>
			            
		        	</form>
	            </div>
        </div>
        
         <!-- Back Button -->
        <a href="/LogoutServlet" class="back-button">Logout</a>
    </div>
    
    <script type="text/javascript">
	    function submitSearchForm(type) {
	        // Set values based on selected type
	        const searchTypeField = document.getElementById("searchType");
	        const searchCriteriaField = document.getElementById("searchCriteria");
	
	        let inputValue = "";
	
	        if (type === "name") {
	            inputValue = document.getElementById("nameInput").value.trim();
	        } else if (type === "city") {
	            inputValue = document.getElementById("nearestCity").value.trim();
	        } else if (type === "highway") {
	            inputValue = document.getElementById("highway").value.trim();
	        } else if (type === "phone") {
	            inputValue = document.getElementById("phone").value.trim();
	        }
	        
	
	        if (inputValue === "") {
	            alert("Please enter a value to search.");
	            return;
	        }
	
	        // Set the hidden fields
	        searchTypeField.value = type;
	        searchCriteriaField.value = inputValue;
	
	        // Submit the form
	        document.getElementById("searchForm").submit();
	    }
    
	    const cityList = [
	        <c:forEach var="city" items="${cities}" varStatus="status">
	            "${city}"<c:if test="${!status.last}">,</c:if>
	        </c:forEach>
	    ];
	    
	    const input1 = document.getElementById('nearestCity');
		const suggestionBox1 = document.getElementById('citySuggestions');
		
		input1.addEventListener('input', function () {
			
	        const value = this.value.trim().toLowerCase();
	        console.log(value);
	        suggestionBox1.innerHTML = '';  // Clear old suggestions
	        if (value === '') {
	            suggestionBox1.style.display = 'none';
	            return;
	        }
	
	        const matches = cityList.filter(city => city.toLowerCase().startsWith(value));
	        console.log(matches);
	        if (matches.length === 0) {
	            suggestionBox1.style.display = 'none';
	            return;
	        }
	
	        matches.forEach(city => {
	            const div = document.createElement('div');
	            div.textContent = city;
	            div.style.padding = '8px';
	            div.style.cursor = 'pointer';
	            div.addEventListener('click', () => {
	                input1.value = city;
	                suggestionBox1.style.display = 'none';
	            });
	            suggestionBox1.appendChild(div);
	        });
	
	        const rect = input1.getBoundingClientRect();
	        suggestionBox1.style.top = (input1.offsetTop + input1.offsetHeight) + 'px';
	        suggestionBox1.style.left = input1.offsetLeft + 'px';
	        suggestionBox1.style.display = 'block';
	    });
	
		// Hide suggestions on click outside
	    document.addEventListener('click', function (e) {
	        if (e.target !== input1 && e.target.parentNode !== suggestionBox1) {
	            suggestionBox1.style.display = 'none';
	        }
	    });
		
	    const highwayList = [
	    	<c:forEach var="highway" items="${highways}" varStatus="status">
	        	"${highway}"<c:if test="${!status.last}">,</c:if>
	    	</c:forEach>
	    ];
	    const input2 = document.getElementById('highway');
		const suggestionBox2 = document.getElementById('highwaySuggestions');
		
		input2.addEventListener('input', function () {
			
	        const value = this.value.trim().toLowerCase();
	        console.log(value);
	        suggestionBox2.innerHTML = '';  // Clear old suggestions
	        if (value === '') {
	            suggestionBox2.style.display = 'none';
	            return;
	        }
	
	        const matches = highwayList.filter(highway => highway.toLowerCase().startsWith(value));
	        console.log(matches);
	        if (matches.length === 0) {
	            suggestionBox2.style.display = 'none';
	            return;
	        }
	
	        matches.forEach(highway => {
	            const div = document.createElement('div');
	            div.textContent = highway;
	            div.style.padding = '8px';
	            div.style.cursor = 'pointer';
	            div.addEventListener('click', () => {
	                input2.value = highway;
	                suggestionBox2.style.display = 'none';
	            });
	            suggestionBox2.appendChild(div);
	        });
	
	        const rect = input2.getBoundingClientRect();
	        suggestionBox2.style.top = (input2.offsetTop + input2.offsetHeight) + 'px';
	        suggestionBox2.style.left = input2.offsetLeft + 'px';
	        suggestionBox2.style.display = 'block';
	    });
		
		// Hide suggestions on click outside
	    document.addEventListener('click', function (e) {
	        if (e.target !== input2 && e.target.parentNode !== suggestionBox2) {
	            suggestionBox2.style.display = 'none';
	        }
	    });
		
	    
		
    </script>
</body>
</html>
