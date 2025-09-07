<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Search Results</title>
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
		
		
		.container {
			width: 80%;
			margin: 10px auto;
		}
		
		h2 {
			text-align: center;
		}
		
		table {
			width: 80%;
			white-space: normal;
			border-collapse: collapse;
			margin-top: 20px;
			table-layout: fixed;
 			
  			
		}
		
		table, th, td {
			border: 1px solid #ddd;
		}
		
		th, td {
			padding: 12px;
			text-align: left;
			
		}
		
		th {
			background-color: #4CAF50;
			color: white;
		}
		
		td {
			background-color: #f9f9f9;
		}
		
		.action-buttons {
			display: flex;
			gap: 10px;
		}
		
		.edit-button, .delete-button {
			padding: 4px 12px;
			border: none;
			color: white;
			cursor: pointer;
			border-radius: 5px;
			text-decoration: none;
		}
		
		.edit-button {
			background-color: #2196F3;
		}
		
		.edit-button:hover {
			background-color: #1E88E5;
		}
		
		.delete-button {
			background-color: #f44336;
		}
		
		.delete-button:hover {
			background-color: #e53935;
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
		/* Popup Styling */
		.popup {
			display: none;
			position: fixed;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			width: 90%; /* Make it responsive */
    		max-width: 700px; /* Prevents it from getting too wide on large screens */
    		max-height: 80vh; /* Restricts the height to 80% of the viewport */
    		background: white;
			box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
			padding: 30px;
			border-radius: 10px;
			z-index: 1000;
			overflow-y: auto; /* Enables vertical scrolling if content overflows */
		}
		.form-container {
		    display: flex;
		    justify-content: space-between;
		    gap: 20px;
		}
		
		.column {
		    width: 48%;
		}
		.popup h3 {
			text-align: center;
		}
		
		.popup input {
			width: auto;
			padding: 5px;
			margin: 5px 0;
			border: 1px solid #ddd;
			border-radius: 5px;
		}
		.popup label {
			padding: 1px;
			margin: 1px 0;
			display: inline-block;
    		width: 180px; /* Adjust the width as needed */
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
		
		.popup-buttons {
			text-align: center;
			margin-top: 10px;
		}
		
		.submit-button, .cancel-button {
			padding: 10px 15px;
			border: none;
			color: white;
			cursor: pointer;
			border-radius: 5px;
			margin: 5px;
		}
		
		.submit-button {
			background-color: #4CAF50;
		}
		
		.submit-button:hover {
			background-color: #45a049;
		}
		
		.cancel-button {
			background-color: #f44336;
		}
		
		.cancel-button:hover {
			background-color: #e53935;
		}
		
		.message {
			margin: 20px 0;
			padding: 15px;
			border-radius: 5px;
			text-align: center;
		}
		
		.success-message {
			background-color: #d4edda; /* Green for success */
			color: #155724; /* Dark green text */
		}
		
		.error-message {
			background-color: #f8d7da; /* Red for error */
			color: #721c24; /* Dark red text */
		}
		
		/* Ensure popup stays centered on smaller screens */
		@media screen and (max-width: 600px) {
		    .popup {
		        top: 20%;
		        
		    }
		}
	</style>
</head>
<body>
	<div class="container">

		<%@ page import="java.util.List"%>
		<%@ page import="java.util.Arrays"%>
		<%
		@SuppressWarnings("unchecked")
		List<String[]> results = (List<String[]>) request.getAttribute("results");
		%>
		<h2>Search Results</h2>

		<!-- Display success or failure message -->
		<c:if test="${not empty message}">
			<div
				class="message 
	            <c:choose>
	                <c:when test="${message == 'Data updated successfully!' || message == 'Record deleted!'}">success-message</c:when>
	                <c:otherwise>error-message</c:otherwise>
	            </c:choose>
	        ">
				${message}</div>
		</c:if>

		<!-- Table to display search results -->
		<table>
			<colgroup>
			  <col style="width: 60px;">    <!-- ID -->
			  <col style="width: 140px;">   <!-- Name -->
			  <col style="width: 200px;">   <!-- Address -->
			  <col style="width: 120px;">   <!-- Contact No -->
			  <col style="width: 120px;">   <!-- Contact 2 -->
			  <col style="width: 120px;">   <!-- Contact 3 -->
			  <col style="width: 140px;">   <!-- Nearest City -->
			  <col style="width: 80px;">   <!-- Highway -->
			  <col style="width: 150px;">   <!-- Website -->
			  <col style="width: 100px;">   <!-- Vehicle Category -->
			  <col style="width: 100px;">   <!-- Services Provided -->
			  <col style="width: 150px;">   <!-- Remarks -->
			  <col style="width: 140px;">   <!-- Buttons -->
			</colgroup>
			
			<thead>
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Address</th>
					<th>Contact No</th>
					<th>Contact 2</th>
					<th>Contact 3</th>
					<th>Nearest City</th>
					<th>Highway</th>
					<th>Web site</th>
					<th>Vehicle Category</th>
					<th>Services Provided</th>
					<th>Remarks</th>
					<th></th> <!-- New Column for Buttons -->
				</tr>
			</thead>
			<tbody>
				<% if (results.size() > 0) {
						for (String[] row : results) { %>
				<tr>
					<% for (String data : row) { %>
					<td><%=data%></td>
					<% } %>
					<!-- Action Buttons -->
					<td>
						<div class="action-buttons">
							<button class="edit-button" onclick="openEditPopup(
								<%= row[0] %>, '<%= row[1] %>', '<%= row[2] %>', '<%= row[3] %>',
							 	'<%= row[4] %>', '<%= row[5] %>', '<%= row[6] %>', '<%= row[7] %>',
							 	'<%= row[8] %>', '<%= row[9] %>', '<%= row[10] %>', '<%= row[11] %>')">Edit</button>
							<button class="delete-button" onclick="openDeletePopup('<%= row[0] %>')">Delete</button>

						</div>
					</td>
				</tr>
				<% } } else { %>
				<tr>
					<td colspan="6" style="text-align: center; color: red;">No
						results found.</td>
				</tr>
				<% } %>
			</tbody>
		</table>

		<!-- Edit Popup -->
		<div id="editPopup" class="popup">
			<h3>Edit Details</h3>
			
			<form id="editForm" action="AdminSearchServlet" method="post">
				<input type="hidden" name="action" value="edit"> 
				<input type="hidden" id="editIndex" name="index"> 
				
				<div class="form-container">
				    <div class="column">
				        <label for="garageName">Garage Name:</label>
				        <input type="text" id="editName" name="name"><br> 
				        
				        <label for="contactNumber">Contact Number:</label>
				        <input type="text" id="editContact" name="contact"><br>
				        
				        <label for="contactNumber3">Contact Number 3:</label>
				        <input type="text" id="editContact3" name="contact3"><br>
				        
				        <label for="highway">Highway:</label>
				        <input type="text" id="editHighway" name="highway"><br>
				        
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
						
						<label>Remarks:</label>
						<input type="text" id="editField12" name="field12"><br>
				    </div>
				    
				    <div class="column">
				    	<label for="address">Address:</label>
				        <input type="text" id="editAddress" name="address"><br>
				        
				        <label for="contactNumber2">Contact Number 2:</label>
				        <input type="text" id="editContact2" name="contact2"><br>
				        
				         <label for="nearestCity">Nearest City:</label>
				        <input type="text" id="editCity" name="city"><br>
				        
				        <label for="website">Web site:</label>
				        <input type="text" id="editWebsite" name="website"><br>
				        
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
				    </div>
				</div>

				<div class="popup-buttons">
					<button type="submit" class="submit-button">Save Changes</button>
					<button type="button" class="cancel-button" onclick="closeEditPopup()">Cancel</button>
				</div>
			</form>
		</div>
		
		<div id="deletePopup" class="popup">
			<h3>Delete Record</h3>
			<form id="deleteForm" action="AdminSearchServlet" method="post">
				<input type="hidden" name="action" value="delete">
				<input type="hidden" id="deleteIndex" name="index"> 
				<label>Are you sure you want to delete this record?</label>
				
				<div class="popup-buttons">
					<button type="submit" class="submit-button">Delete Record</button>
					<button type="button" class="cancel-button" onclick="closeDeletePopup()">Cancel</button>
				</div>
			</form>
		</div>

		<!-- Back Button -->
		<a href="welcome.jsp" class="back-button">Back to Home</a>

		<script>
	        function openEditPopup(index, name, address, contact, contact2, contact3, city, highway, website, vehicleCategory, servicesProvided, field12) {
	        	console.log('openEditPopup');
	            document.getElementById("editIndex").value = index;
	            document.getElementById("editName").value = name;
	            document.getElementById("editAddress").value = address;
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
	</div>
</body>
</html>
