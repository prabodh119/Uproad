<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
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
        .login-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        .login-container h2 {
            margin-bottom: 20px;
        }
        .input-field {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .login-button, .signup-button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .login-button:hover, .signup-button:hover {
            background-color: #45a049;
        }
        .signup-link {
            color: #007bff;
            text-decoration: none;
            display: block;
            margin-top: 10px;
            cursor: pointer;
        }
        .signup-link:hover {
            text-decoration: underline;
        }
        .error-message {
            color: red;
            font-size: 12px;
            margin-top: 10px;
        }
        
        /* Sign-up Popup Modal */
        .modal {
            display: none;
            position: absolute;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.3);
        }
        .modal-content {
            background-color: white;
            margin: 3% auto;
            padding: 20px;
            border-radius: 8px;
            width: 350px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
            text-align: left;
        }
        .close {
            float: right;
            font-size: 20px;
            font-weight: bold;
            cursor: pointer;
        }
        
        .message {
            margin: 20px;
            border-radius: 5px;
            text-align: center;
        }

        .success-message {
            background-color: #d4edda;  /* Green for success */
            color: #155724;              /* Dark green text */
        }

        .error-message {
        	padding: 5px;
            background-color: #f8d7da;  /* Red for error */
            color: #721c24;              /* Dark red text */
        }
    </style>
</head>
<body>

<div class="login-container">

	<!-- Display success or failure message -->
    <c:if test="${not empty message}">
        <div class="message 
            <c:choose>
                <c:when test="${message == 'Successfully Registered. Please Login.'}">success-message</c:when>
                <c:otherwise>error-message</c:otherwise>
            </c:choose>
        ">
            ${message}
        </div>
    </c:if>
        
    <h2>Login</h2>
    <form action="/uproad/LoginServlet" method="POST">
        <input type="text" name="username" class="input-field" placeholder="Username" required>
        <input type="password" name="password" class="input-field" placeholder="Password" required>
        <button type="submit" class="login-button">Login</button>
    </form>

    <!-- Sign-up link -->
    <a class="signup-link" onclick="openSignupModal()">New User? Sign Up</a>

    <%-- Display login error message if exists --%>
    <% 
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) { 
    %>
        <div class="error-message"><%= errorMessage %></div>
    <% } %>
</div>

<!-- Sign-up Modal -->
<div id="signupModal" class="modal">

    <div class="modal-content">
        <span class="close" onclick="closeSignupModal()">&times;</span>
        <h2>Sign Up</h2>
        
        <form id="signupForm" action="/uproad/SignupServlet" method="POST" onsubmit="return validatePassword()">
            <label for="name">Name:</label>
            <input type="text" name="name" class="input-field" required>

            <label for="telephone">Telephone Number:</label>
            <input type="text" name="telephone" class="input-field" required>

            <label for="idNumber">ID Number (Username):</label>
            <input type="text" name="idNumber" class="input-field" required>
            
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" class="input-field" required>

            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="input-field" required>

            <div class="error-message" id="passwordError" style="display: none;"></div>

            <label for="vehicleMake">Vehicle Make:</label>
            <input type="text" name="vehicleMake" class="input-field" required>

            <label for="vehicleModel">Vehicle Model:</label>
            <input type="text" name="vehicleModel" class="input-field" required>

            <label for="vehicleYear">Vehicle Year:</label>
            <input type="text" name="vehicleYear" class="input-field" required>
            
            <label for="vehicleCategory">Vehicle Category:</label>
			<select name="vehicleCategory" id="vehicleCategoryDropdown" class="input-field" required>
			    <%-- <option value="" disabled selected>Select Vehicle Type</option>
			    <c:forEach var="category" items="${categories}">
			        <option value="${category.vehicleCategory}">${category.vehicleCategory}</option>
			    </c:forEach> --%>
			     <option value="" disabled selected>Select Vehicle Type</option>
			</select>

            <button type="submit" class="signup-button">Sign Up</button>
        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function () {
	    $.ajax({
	        url: '/uproad/VehicleCategoryServlet', // URL of servlet
	        type: 'GET',
	        dataType: 'json',
	        success: function (data) {
	            var select = $('select[name="vehicleCategory"]');
	            select.empty(); // Clear existing options
	            select.append('<option value="" disabled selected>Select Vehicle Type</option>');
	
	            $.each(data, function (index, category) {
	                select.append('<option value="' + category + '">' + category + '</option>');
	            });
	        },
	        error: function () {
	            console.error('Error fetching vehicle categories.');
	        }
	    });
	});

    function openSignupModal() {
        document.getElementById("signupModal").style.display = "block";
    }

    function closeSignupModal() {
        document.getElementById("signupModal").style.display = "none";
    }

    function validatePassword() {
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("confirmPassword").value;
        var errorDiv = document.getElementById("passwordError");

        if (password !== confirmPassword) {
            errorDiv.style.display = "block";
            errorDiv.innerText = "Passwords do not match!";
            return false;
        } else {
            errorDiv.style.display = "none";
            return true;
        }
    }
</script>

</body>
</html>
