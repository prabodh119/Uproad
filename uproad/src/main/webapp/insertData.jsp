<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insert Garage Information</title>
    <style>
    	* { 
			box-sizing: border-box; 
		}
		
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        
        h2 {
            text-align: center;
        }
        
        .container {
            max-width: 700px;
            margin: 10px auto;
            
        }
        .form-container {
            background-color: #ffffff;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        }
	    
	    input {
			width: auto;
			padding: 5px;
			margin: 5px 0;
			border: 1px solid #ddd;
			border-radius: 5px;
		}
		
		input[type="text"] {
			width: 100%;
		}
		
		label {
			padding: 1px;
			margin: 1px 0;
			display: inline-block;
		}
		
		/* Narrower divs for vehicleCategory and servicesProvided */
        #vehicleCategory, #servicesProvided {
            padding: 10px;
            background-color: #f9f9f9;
            border: 1px solid #ddd;
            display: block; /* Makes it behave like a block element (to take full width) */
            width: auto;
            box-sizing: border-box; /* Include padding and borders in the element's total width */
            margin-bottom: 10px; /* Add margin for spacing between elements */
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
        
        .message {
            margin: 20px 0;
            padding: 15px;
            border-radius: 5px;
            text-align: center;
        }
        
        .success-message {
            background-color: #d4edda;  /* Green for success */
            color: #155724;              /* Dark green text */
        }
        
        .error-message {
            background-color: #f8d7da;  /* Red for error */
            color: #721c24;              /* Dark red text */
        }
        
        .submit-btn {
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 20px; /* Add space above the button */
        }
        
        .submit-btn:hover {
            background-color: #0056b3;
        }
        
        /* Ensure popup stays centered on smaller screens */
		 @media screen and (max-width: 768px) {
            .header {
                font-size: 20px;
                padding: 12px;
            }

            th, td {
                font-size: 12px;
                padding: 6px;
            }

            .popup input[type="text"] {
                font-size: 14px;
            }

            .popup-buttons button, .delete-buttons button {
                font-size: 14px;
            }
        } 
    </style>
    <script type="text/javascript">
	    function isValidPhone(value, required) {
	        value = value.trim();
	
	        // If empty
	        if (value === "") {
	            return !required; // valid only if not required
	        }
	
	        // Digits only
	        if (!/^\d+$/.test(value)) {
	            return false;
	        }
	
	        // Length check
	        return value.length === 9 || value.length === 10;
	    }
		
        // Function to confirm form submission
        function confirmSubmission() {
            var garageName = document.getElementById("garageName").value.trim();
            var address = document.getElementById("address").value.trim();
            var location = document.getElementById("field13").value.trim();
            
            var contactNumber = document.getElementById("contactNumber").value.trim();
            var contactNumber2 = document.getElementById("contactNumber2").value.trim();
            var contactNumber3 = document.getElementById("contactNumber3").value.trim();
            
            var nearestCity = document.getElementById("nearestCity").value.trim();
            var highway = document.getElementById("highway").value.trim();
            var website = document.getElementById("website").value.trim();
            
            const vehicles = document.querySelectorAll('#vehicleCategory input[type="checkbox"]');
            const isChecked1 = Array.from(vehicles).some(checkbox => checkbox.checked);
            const selectedVehicles = Array.from(vehicles).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
            
            const services = document.querySelectorAll('#servicesProvided input[type="checkbox"]');
            const isChecked2 = Array.from(services).some(checkbox => checkbox.checked);
            const selectedServices = Array.from(services).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
            
            var remarks = document.getElementById("field12").value.trim(); 
            
            /* ---------- BASIC REQUIRED FIELD CHECK ---------- */
            if (!garageName || !address || !nearestCity || !highway) {
                alert("Please fill in all required fields.");
                return;
            }

            /* ---------- PHONE VALIDATION ---------- */
            if (!isValidPhone(contactNumber, true)) {
                alert("Primary contact number must be 9 or 10 digits.");
                return;
            }

            if (!isValidPhone(contactNumber2, false)) {
                alert("Contact Number 2 must be 9 or 10 digits if provided.");
                return;
            }

            if (!isValidPhone(contactNumber3, false)) {
                alert("Contact Number 3 must be 9 or 10 digits if provided.");
                return;
            }
            
            var confirmMessage = "Are you sure you want to submit the following data?\n\n";
            confirmMessage += "Garage Name: " + garageName + "\n";
            confirmMessage += "Address: " + address + "\n";
            confirmMessage += "Location: " + location + "\n";
            confirmMessage += "Contact Number: " + contactNumber + "\n";
            confirmMessage += "Contact Number 2: " + contactNumber2 + "\n";
            confirmMessage += "Contact Number 3: " + contactNumber3 + "\n";
            confirmMessage += "Nearest City: " + nearestCity + "\n";
            confirmMessage += "Highway: " + highway + "\n";
            confirmMessage += "Web site: " + website + "\n";
            confirmMessage += "Vehicle Categories: " + selectedVehicles.join(', ') + "\n";
            confirmMessage += "Services Provided: " + selectedServices.join(', ') + "\n";
            confirmMessage += "Remarks: " + remarks + "\n";
            
            // Show confirmation popup
            if (confirm(confirmMessage)) {
                document.getElementById("garageForm").submit();
            }
            
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Insert Garage Information</h2>
        
        <!-- Display success or failure message -->
        <c:if test="${not empty message}">
            <div class="message 
                <c:choose>
                    <c:when test="${message == 'Garage information successfully inserted.'}">success-message</c:when>
                    <c:otherwise>error-message</c:otherwise>
                </c:choose>
            ">
                ${message}
            </div>
        </c:if>
        
        <div class="form-container">
            <form id="garageForm" method="post" action="/uproad/InsertDataServlet">

                <label for="garageName">Garage Name:</label><br>
                <input type="text" id="garageName" name="garageName" required><br>
                
                <label for="address">Address:</label><br>
                <input type="text" id="address" name="address" required><br>
                
                <label for="field13">Location:</label><br>
                <input type="text" id="field13" name="field13" required><br>
            
                <label for="contactNumber">Contact Number:</label><br>
                <input type="text" id="contactNumber" name="contactNumber" inputmode="numeric" pattern="[0-9]{9,10}" required><br>
                
                <label for="contactNumber2">Contact Number 2:</label><br>
                <input type="text" id="contactNumber2" name="contactNumber2" inputmode="numeric" pattern="[0-9]{9,10}"><br>
            	
            	<label for="contactNumber3">Contact Number 3:</label><br>
                <input type="text" id="contactNumber3" name="contactNumber3" inputmode="numeric" pattern="[0-9]{9,10}"><br>
                
                <label for="nearestCity">Nearest City:</label><br>
                <input type="text" id="nearestCity" name="nearestCity" required><br>
                <div id="citySuggestions"></div>
            
            	<label for="highway">Highway:</label><br>
                <input type="text" id="highway" name="highway" required><br>
                
                <label for="website">Web site:</label><br>
                <input type="text" id="website" name="website" required><br>
                
                <label for="vehicleCategory">Vehicle Category:</label><br>
				<div id="vehicleCategory">
				    <!-- Iterate over the 'categories' request attribute (ArrayList of VehicleCategory objects) -->
				    <c:forEach var="category" items="${categories}">
				        <label>
				            <input type="checkbox" name="vehicleCategory[]" value="${category.vehicleCategory}">
				            ${category.vehicleCategory}
				        </label><br>
				    </c:forEach>
				</div>
				
				<label for="servicesProvided">Services Provided:</label><br>
				<div id="servicesProvided">
				    <!-- Iterate over the 'services' request attribute (ArrayList of ServiceType objects) -->
				    <c:forEach var="service" items="${services}">
				        <label>
				            <input type="checkbox" name="servicesProvided[]" value="${service.serviceType}">
				            ${service.serviceType}
				        </label><br>
				    </c:forEach>
				</div>
				
				<label for="field12">Remarks:</label><br>
              		<input type="text" id="field12" name="field12" required>
		                      
                <button type="button" class="submit-btn" onclick="confirmSubmission()">Submit</button>
            </form>
        </div>
        
        <a href="welcome.jsp" class="back-button">Back</a>
    </div>
    
    <script type="text/javascript">
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
    </script>
</body>
</html>
