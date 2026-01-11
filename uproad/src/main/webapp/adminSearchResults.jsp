<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Results</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
    	* { 
			box-sizing: border-box; 
		}
        body {
            font-family: Arial, sans-serif;
            font-size: 15px;
            margin: 0;
            padding: 0;
            background: #f4f4f4;
        }
        .header {
            top: 0;
		    left: 0;
		    width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 15px;
            text-align: center;
            font-size: 24px;
            position: fixed;
        }
        .back-button {
		    float: left;
		    background-color: white;
		    color: #4CAF50;
		    border: none;
		    padding: 8px 16px;
		    margin-right: 15px;
		    font-size: 16px;
		    cursor: pointer;
		    border-radius: 4px;
		}
		.back-button:hover {
		    background-color: #f1f1f1;
		}
		.table-container {
			padding: 10px;
		}
        .table-container::-webkit-scrollbar {
		    height: 8px;
		}
		.table-container::-webkit-scrollbar-thumb {
		    background-color: #888;
		    border-radius: 4px;
		}
		.table-container::-webkit-scrollbar-track {
		    background-color: #f1f1f1;
		}
		table {
            border-collapse: collapse;
            background: white;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        .action-buttons {
            display: flex;
            justify-content: center;
            gap: 8px;
        }
        .action-buttons button {
            padding: 5px 10px;
            border: none;
            color: white;
            cursor: pointer;
            border-radius: 5px;
            font-size: 14px;
        }
        .edit-btn {
            background-color: #2196F3;
        }
        .delete-btn {
            background-color: #f44336;
        }
        .edit-btn:hover {
            background-color: #1976D2;
        }
        .delete-btn:hover {
            background-color: #d32f2f;
        }
        /* Popups */
        .popup, .delete-popup {
            display: none;
            position: fixed;
            top: 50%; left: 50%;
            transform: translate(-50%, -50%);
            background: white;
            padding: 20px;
            border-radius: 10px;
            z-index: 1000;
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            overflow-y: auto;
            box-shadow: 0 0 15px rgba(0,0,0,0.3);
        }
        .popup h3, .delete-popup h3 {
            text-align: center;
            margin-top: 0;
        }
		.popup label {
			padding: 1px;
			margin: 1px 0;
			display: inline-block;
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
        .popup-buttons, .delete-buttons {
            text-align: center;
            margin-top: 10px;
        }
        .popup-buttons button,
        .delete-buttons button {
            margin: 5px;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
            font-size: 14px;
        }	
		#vehicleCategory, #servicesProvided {
            padding: 10px;
            background-color: #f9f9f9;
            border: 1px solid #ddd;
            display: block; /* Makes it behave like a block element (to take full width) */
            width: auto;
            box-sizing: border-box; /* Include padding and borders in the element's total width */
            margin-bottom: 10px; /* Add margin for spacing between elements */
        }      
        .save-btn {
            background-color: #4CAF50;
        }
        .cancel-btn {
            background-color: #f44336;
        }
        .confirm-delete-btn {
            background-color: #d32f2f;
        }
        .overlay {
            display: none;
            position: fixed;
            top: 0; left: 0;
            width: 100vw; height: 100vh;
            background: rgba(0, 0, 0, 0.4);
            z-index: 999;
        }
		.message {
			padding: 15px;
			border-radius: 5px;
			text-align: center;
			margin-top: 74px;
		}
		.message.hidden + .table-container {
		    margin-top: 64px;
		}
		.success-message {
			background-color: #d4edda; /* Green for success */
			color: #155724; /* Dark green text */
		}	
		.error-message {
			background-color: #f8d7da; /* Red for error */
			color: #721c24; /* Dark red text */
		}	
        @media screen and (max-width: 768px) {
            .header {
                font-size: 20px;
                padding: 12px;
            }       
            .back-button {
            	font-size: 12px;
                padding: 6px 12px;
            }
			.table-container {
	            width: 100%;
	            overflow-x: auto;
	            padding: 10px;   
        	}
        	.message.hidden + .table-container {
			    margin-top: 49px;
			}
            th, td {
                font-size: 12px;
                padding: 6px;
            }
            .popup input[type="text"] {
                font-size: 12px;
            }
            .popup-buttons button, .delete-buttons button {
                font-size: 12px;
            }
        }
    </style>
</head>
<body>
	<%@ page import="java.util.List"%>
	<%@ page import="java.util.Arrays"%>
	<%
		@SuppressWarnings("unchecked")
		List<String[]> results = (List<String[]>) request.getAttribute("results");
		
	%>
		
    <div class="header">
    	<a href="welcome.jsp" class="back-button">Back</a>
        Search Results
    </div>

	<!-- Display success or failure message -->
	<c:choose>
		<c:when test="${not empty message}">
			<div class="message 
	            <c:choose>
	                <c:when test="${message == 'Data updated successfully!' || message == 'Record deleted!'}">success-message</c:when>
	                <c:otherwise>error-message</c:otherwise>
	            </c:choose>
	        "> ${message}</div>
		</c:when>
		<c:otherwise>
	        <div class="message hidden" style="display: none;"></div>
	    </c:otherwise>
	</c:choose>
	
    <div class="table-container">
        <table>
            <thead>
                <tr>
					<th>ID</th>
					<th>Name</th>
					<th>Address</th>
					<th>Location</th>
					<th>Contact No</th>
					<th>Contact 2</th>
					<th>Contact 3</th>
					<th>Nearest City</th>
					<th>Highway</th>
					<th>Web site</th>
					<th>Vehicle Category</th>
					<th>Services Provided</th>
					<th>Remarks</th>
					<th>Rating</th>
					<th>Actions</th> <!-- New Column for Buttons -->
				</tr>
            </thead>
            <tbody>
                <% if (results.size() > 0) {
                		for (String[] row : results) { %>
				<tr>
					<% for (int i = 0; i < row.length; i++) { %>
					<td>
						<% 
							if (i == 2 || i == 3 || i == 9) { // Address column, location, web site %>
								<a href="<%= row[i] %>" target="_blank"><%= row[i] %></a>
						<% 
							} else if (i == 4 || i == 5 || i == 6) { // Phone numbers
					            String phone = row[i].replaceAll("[^\\d+]", ""); // Remove non-numeric characters except +
					            if (!phone.isEmpty()) {
								%>
									<a href="tel:<%= phone %>"><%= row[i] %></a>
								<% 
            					} else {
   						 		%>
									<%= row[i] %>
								<% } %>
						<% 
							} else {
						%>
								<%= row[i] %>
						<% 	} %>
					</td>
					<% } %>
					<!-- Action Buttons -->
					<td>
						<div class="action-buttons">
							 <button class="edit-btn"
							    data-index="<%= row[0] %>"
							    data-name="<%= row[1] %>"
							    data-address="<%= row[2] %>"
							    data-field13="<%= row[3] %>"
							    data-contact="<%= row[4] %>"
							    data-contact2="<%= row[5] %>"
							    data-contact3="<%= row[6] %>"
							    data-city="<%= row[7] %>"
							    data-highway="<%= row[8] %>"
							    data-website="<%= row[9] %>"
							    data-vehiclecategory="<%= row[10] %>"
							    data-servicesprovided="<%= row[11] %>"
							    data-field12="<%= row[12] %>"
							    onclick="editFromData(this)">
							    Edit
							</button>
							<button class="delete-btn" onclick="openDeletePopup('<%= row[0] %>')">Delete</button>

						</div>
					</td>
				</tr>
				<% } 
				} else { %>
				<tr>
					<td colspan="6" style="text-align: center; color: red;">No results found.</td>
				</tr>
				<% } %>
            </tbody>
        </table>
    </div>

    <!-- Overlay -->
    <div class="overlay" id="overlay"></div>

    <!-- Edit Popup -->
    <div class="popup" id="editPopup">
        <h3>Edit Details</h3>
			
		<form id="editForm" action="AdminSearchServlet" method="post">
			<input type="hidden" name="action" value="edit"> 
			<input type="hidden" id="editIndex" name="index"> 
			<input type="hidden" name="searchType" value="${type}">
			<input type="hidden" name="searchCriteria" value="${criteria}">
			
	        <label for="garageName">Garage Name:</label>
	        <input type="text" id="editName" name="name"><br> 
	        
	        <label for="address">Address:</label>
	        <input type="text" id="editAddress" name="address"><br>
	        
	        <label for="field13">Location:</label>
	        <input type="text" id="editField13" name="field13"><br>
	        
	        <label for="contactNumber">Contact Number:</label>
	        <input type="text" id="editContact" name="contact" inputmode="numeric" pattern="[0-9]{9,10}" required><br>
	        
	        <label for="contactNumber2">Contact Number 2:</label>
	        <input type="text" id="editContact2" name="contact2" inputmode="numeric" pattern="[0-9]{9,10}"><br>
	        
	        <label for="contactNumber3">Contact Number 3:</label>
	        <input type="text" id="editContact3" name="contact3" inputmode="numeric" pattern="[0-9]{9,10}"><br>
	        
	        <label for="nearestCity">Nearest City:</label>
	        <input type="text" id="editCity" name="city"><br>
	        
	        <label for="highway">Highway:</label>
	        <input type="text" id="editHighway" name="highway"><br>
	        
	        <label for="website">Web site:</label>
	        <input type="text" id="editWebsite" name="website"><br>
	        
	        <!-- Vehicle Category Checkboxes -->
			<label for="vehicleCategory">Vehicle Category:</label>
			<div id="vehicleCategory">
			    <!-- Iterate over the 'categories' request attribute (ArrayList of VehicleCategory objects) -->
			    <c:forEach var="category" items="${categories}">
			        <label>
			            <input type="checkbox" name="vehicleCategory[]" value="${category.vehicleCategory}">
			            ${category.vehicleCategory}
			        </label><br>
			    </c:forEach>
			</div>
			
			<label for="servicesProvided">Services Provided:</label>
			<div id="servicesProvided">
			    <!-- Iterate over the 'services' request attribute (ArrayList of ServiceType objects) -->
			    <c:forEach var="service" items="${services}">
			        <label>
			            <input type="checkbox" name="servicesProvided[]" value="${service.serviceType}">
			            ${service.serviceType}
			        </label><br>
			    </c:forEach>
			</div>
			
			<label>Remarks:</label>
			<input type="text" id="editField12" name="field12"><br>
			 
			<div class="popup-buttons">
				<button type="button" class="save-btn" onclick="confirmSubmission()">Save Changes</button>
				<button type="button" class="cancel-btn" onclick="closeEditPopup()">Cancel</button>
			</div>
		</form>
    </div>

    <!-- Delete Confirmation Popup -->
    <div class="delete-popup" id="deletePopup">
        <h3>Delete Record</h3>
			<form id="deleteForm" action="AdminSearchServlet" method="post">
				<input type="hidden" name="action" value="delete">
				<input type="hidden" id="deleteIndex" name="index"> 
				<label>Are you sure you want to delete this record?</label>
				
				<div class="popup-buttons">
					<button type="submit" class="save-btn">Delete Record</button>
					<button type="button" class="cancel-btn" onclick="closeDeletePopup()">Cancel</button>
				</div>
			</form>
    </div>

    <script>
	    function editFromData(btn) {
	        // Safely extract data attributes
	        const index = btn.dataset.index;
	        const name = btn.dataset.name;
	        const address = btn.dataset.address;
	        const field13 = btn.dataset.field13;
	        const contact = btn.dataset.contact;
	        const contact2 = btn.dataset.contact2;
	        const contact3 = btn.dataset.contact3;
	        const city = btn.dataset.city;
	        const highway = btn.dataset.highway;
	        const website = btn.dataset.website;
	        const vehicleCategory = btn.dataset.vehiclecategory;
	        const servicesProvided = btn.dataset.servicesprovided;
	        const field12 = btn.dataset.field12;
	
	        // Pass to your existing function
	        openEditPopup(index, name, address, field13, contact, contact2, contact3, city, highway, website, vehicleCategory, servicesProvided, field12);
	    }
    	
    	function openEditPopup(index, name, address, field13, contact, contact2, contact3, city, highway, website, vehicleCategory, servicesProvided, field12) {
	    	console.log('openEditPopup');
	        document.getElementById("editIndex").value = index;
	        document.getElementById("editName").value = name;
	        document.getElementById("editAddress").value = address;
	        document.getElementById("editField13").value = field13;
	        
	        document.getElementById("editContact").value = contact;
	        document.getElementById("editContact2").value = contact2;
	        document.getElementById("editContact3").value = contact3;
	        
	        document.getElementById("editCity").value = city;
	        document.getElementById("editHighway").value = highway;
	        document.getElementById("editWebsite").value = website;
	
	     	// Handle vehicle category checkboxes
	        let vehicleCategories = vehicleCategory.split(",").map(v => v.trim()); // Split and trim spaces
	        document.querySelectorAll("#vehicleCategory input[type='checkbox']").forEach(checkbox => {
	            checkbox.checked = vehicleCategories.includes(checkbox.value);
	        });
	
	        // Handle services provided checkboxes
	        let services = servicesProvided.split(",").map(s => s.trim()); // Split and trim spaces
	        document.querySelectorAll("#servicesProvided input[type='checkbox']").forEach(checkbox => {
	            checkbox.checked = services.includes(checkbox.value);
	        });
	     	
	        document.getElementById("editField12").value = field12;
	        document.getElementById("editPopup").style.display = "block";
	    }
    	
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
    	
    	function confirmSubmission() {
            var garageName = document.getElementById("editName").value.trim();
            var address = document.getElementById("editAddress").value.trim();
            var location = document.getElementById("editField13").value.trim();
            
            var contactNumber = document.getElementById("editContact").value.trim();
            var contactNumber2 = document.getElementById("editContact2").value.trim();
            var contactNumber3 = document.getElementById("editContact3").value.trim();
            
            var nearestCity = document.getElementById("editCity").value.trim();
            var highway = document.getElementById("editHighway").value.trim();
            var website = document.getElementById("editWebsite").value.trim();
            
            const vehicles = document.querySelectorAll('#vehicleCategory input[type="checkbox"]');
            const isChecked1 = Array.from(vehicles).some(checkbox => checkbox.checked);
            const selectedVehicles = Array.from(vehicles).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
            
            const services = document.querySelectorAll('#servicesProvided input[type="checkbox"]');
            const isChecked2 = Array.from(services).some(checkbox => checkbox.checked);
            const selectedServices = Array.from(services).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
            
            var remarks = document.getElementById("editField12").value.trim(); 
            
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
                document.getElementById("editForm").submit();
            }
            
        }
	
	    function closeEditPopup() {
	        document.getElementById("editPopup").style.display = "none";
	    }
	    
	    function openDeletePopup(index) {
	    	console.log('openDeletePopup');
	    	document.getElementById("deleteIndex").value = index;
	    	document.getElementById("deletePopup").style.display = "block";
	    }
	    
	    function closeDeletePopup() {
	        document.getElementById("deletePopup").style.display = "none";
	    }
    </script>
</body>
</html>
