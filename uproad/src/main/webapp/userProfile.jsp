<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.VehicleDetail" %>
<%@ page import="dao.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .profile-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 400px;
            text-align: center;
        }
        .profile-container h2 {
            margin-bottom: 20px;
        }
        .vehicle-info {
            margin-top: 20px;
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            justify-content: center;
        }
        .vehicle-tile {
            background-color: #e9ecef;
            padding: 10px;
            border-radius: 5px;
            width: 150px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            text-align: center;
            font-size: 15px;
            position: relative; /* Needed for checkbox positioning */
            line-height: 1.5;
        }   
		.vehicle-radio {
		    position: absolute;
		    top: 5px;
		    right: 5px;
		    transform: scale(1.2); /* Make radio button slightly larger */
		}
		.vehicle-tile span {
		    margin: 10px 5px;
		}
        .button {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 10px;
        }
        .button:hover {
            background-color: #45a049;
        }
        
        /* Popup Modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            
            justify-content: center;
            align-items: center;
        }
        .modal-content {
        	background-color: white;
        	margin: 15% auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
            width: 300px;
            text-align: center;
        }
        .close {
            float: right;
            font-size: 20px;
            cursor: pointer;
        }
		.input-field {
		    width: 100%;
		    padding: 10px;
		    margin-bottom: 10px; /* Increased space between input fields */
		    border: 1px solid #ccc;
		    border-radius: 4px;
		    box-sizing: border-box;
		}
        .logout-button {
		    padding: 10px 10px;
		    background-color: #d9534f; /* Red color for logout */
		    color: white;
		    border: none;
		    border-radius: 4px;
		    cursor: pointer;
		    margin-top: 20px;
		}
		.logout-button:hover {
		    background-color: #c9302c;
		}	
		.search-container {
		    background-color: white;
		    padding: 10px;
		    border-radius: 8px;
		    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		    
		    text-align: center;
		    margin-top: 20px;
		}	
		.search-container h3 {
		    margin-bottom: 15px;
		}	
		.search-container .input-field {
		    width: 90%;
		    padding:10px;
		    margin-bottom: 10px;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		    box-sizing: border-box;
		}
		
		#citySuggestions {
			background: white;
			border: 1px solid #ccc;
			width: 90%; 
			margin:auto;
			max-height: 150px; 
			overflow-y: auto;
			display: none; 
			z-index: 10;
		}
		
		.search-container .button { 
		    padding: 10px;
		    background-color: #007bff; /* Blue color for search */
		    color: white;
		    border: none;
		    border-radius: 4px;
		    cursor: pointer;
		}
		
		.search-container .button:hover {
		    background-color: #0056b3;
		}

        .error-message {
            color: red;
            font-size: 14px;
            margin-top: 10px;
        }
        
    </style>
</head>
<body>
    <div class="profile-container">
    	<% User loggedInUser = (User) session.getAttribute("user"); %>
    	
        <h2>Welcome, ${user.getName()}!</h2>
        
        <% List<VehicleDetail> vehicleList = (List<VehicleDetail>) loggedInUser.getVehicles(); %>
        
        <h3>Your Vehicles</h3>
        
		<form id="searchForm" action="/SearchServlet" method="GET" onsubmit="return validateVehicleSelection()">
		    <div class="vehicle-info">
		        <% if (vehicleList != null && !vehicleList.isEmpty()) { %>
		            <% for (VehicleDetail vehicle : vehicleList) { %>
		                <div class="vehicle-tile">
		                	<label>
			                    <input type="radio" name="selectedVehicle" value="<%= vehicle.getMake() %>,<%= vehicle.getModel() %>,<%= vehicle.getYear() %>,<%= vehicle.getVehicleCategory() %>" class="vehicle-radio">
			                    <span><strong>Make:</strong> <%= vehicle.getMake() %></span> <br/>
			                    <span><strong>Model:</strong> <%= vehicle.getModel() %></span> <br/>
			                    <span><strong>Year:</strong> <%= vehicle.getYear() %></span> <br/>
		                    </label>
		                </div>
		            <% } %>
		        <% } else { %>
		            <p>No vehicles registered.</p>
		        <% } %>
		    </div>
		
			<!-- Hidden Inputs to Pass User and Vehicle Details -->
    		<input type="hidden" name="username" value="<%= loggedInUser.getUsername() %>">
   		    <div id="errorMessage" class="error-message" style="display: none;">
   		        Please select a vehicle before searching!
   		    </div>
    
		    <!-- Search Garage Section -->
		    <div class="search-container">
		        <h3>Search Garage</h3>
		        
		        <!-- Dropdown for Service Type -->
			    <select name="serviceType" class="input-field" required multiple>
				    <option value="" disabled >Select Service Type</option>
				    <c:forEach var="service" items="${services}">
				        <option value="${service.serviceType}">${service.serviceType}</option>
				    </c:forEach>
				</select>
			    
		        <input type="text" id="nearestCity" name="nearestCity" class="input-field" placeholder="Nearest City" required>
		        <div id="citySuggestions"></div>
		        <button type="submit" class="button">Search</button>
		    </div>
		</form>

        <!-- Add Vehicle Button -->
        <button class="button" onclick="openModal()">Add Vehicle</button>
        <!-- Logout Button -->
		<form action="/LogoutServlet" method="GET">
		    <button type="submit" class="logout-button">Logout</button>
		</form>
        
    </div>

    <!-- Pop-up Modal for Adding Vehicle -->
    <div id="addVehicleModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <h3>Add Vehicle</h3>
            
            <form action="/AddVehicleServlet" method="POST">
                <input type="text" name="vehicleMake" class="input-field" placeholder="Vehicle Make" required><br>
                <input type="text" name="vehicleModel" class="input-field" placeholder="Vehicle Model" required><br>
                <input type="text" name="vehicleYear" class="input-field" placeholder="Vehicle Year" required><br>
                
                <!-- Dropdown for Vehicle Type -->
			    <select name="vehicleCategory" class="input-field" required>
				    <option value="" disabled selected>Select Vehicle Type</option>
				    <c:forEach var="category" items="${categories}">
				        <option value="${category.vehicleCategory}">${category.vehicleCategory}</option>
				    </c:forEach>
				</select>
			    
                <button type="submit" class="button">Add Vehicle</button>
            </form>
        </div>
    </div>
    
    
    <script>
	    const cityList = [
	        <c:forEach var="city" items="${cities}" varStatus="status">
	            "${city}"<c:if test="${!status.last}">,</c:if>
	        </c:forEach>
	    ];
    	
    	const input = document.getElementById('nearestCity');
    	const suggestionBox = document.getElementById('citySuggestions');
    	
    	input.addEventListener('input', function () {
    		
            const value = this.value.trim().toLowerCase();
            console.log(value);
            suggestionBox.innerHTML = '';  // Clear old suggestions
            if (value === '') {
                suggestionBox.style.display = 'none';
                return;
            }

            const matches = cityList.filter(city => city.toLowerCase().startsWith(value));
            console.log(matches);
            if (matches.length === 0) {
                suggestionBox.style.display = 'none';
                return;
            }

            matches.forEach(city => {
                const div = document.createElement('div');
                div.textContent = city;
                div.style.padding = '8px';
                div.style.cursor = 'pointer';
                div.addEventListener('click', () => {
                    input.value = city;
                    suggestionBox.style.display = 'none';
                });
                suggestionBox.appendChild(div);
            });

            const rect = input.getBoundingClientRect();
            suggestionBox.style.top = (input.offsetTop + input.offsetHeight) + 'px';
            suggestionBox.style.left = input.offsetLeft + 'px';
            suggestionBox.style.display = 'block';
        });
    	
    	// Hide suggestions on click outside
        document.addEventListener('click', function (e) {
            if (e.target !== input && e.target.parentNode !== suggestionBox) {
                suggestionBox.style.display = 'none';
            }
        });
    
        function openModal() {
            document.getElementById('addVehicleModal').style.display = 'block';
        }
        
        function closeModal() {
            document.getElementById('addVehicleModal').style.display = 'none';
        }

        function validateVehicleSelection() {
            var selectedVehicle = document.querySelector('input[name="selectedVehicle"]:checked');
            if (!selectedVehicle) {
                document.getElementById('errorMessage').style.display = 'block';
                return false;  // Prevent form submission
            }
            return true;
        }
    </script>
</body>
</html>
