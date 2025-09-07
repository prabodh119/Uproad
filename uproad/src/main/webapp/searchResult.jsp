<%@ page import="java.util.List" %>
<%@ page import="dao.Garage" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            padding: 20px;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 80%;
            max-width: 800px;
            text-align: center;
        }
        .garage-list {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 15px;
            margin-top: 20px;
        }
        .garage-tile {
            background-color: #e9ecef;
            padding: 15px;
            border-radius: 8px;
            width: 300px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            text-align: left;
        }
        .garage-tile h3 {
            margin-bottom: 10px;
            color: #007bff;
        }
        .garage-tile p {
            margin: 5px 0;
        }
        .button {
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
            text-decoration: none;
            display: inline-block;
        }
        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>Garage Search Results</h2>

        <% 
            @SuppressWarnings("unchecked")
            List<Garage> garages = (List<Garage>) request.getAttribute("garages");
        %>

        <div class="garage-list">
            <% if (garages == null || garages.isEmpty()) { %>
                <p>No garages found for the selected service in the given city.</p>
            <% } else { %>
                <% for (Garage garage : garages) { %>
                    <div class="garage-tile">
                        <h3><%= garage.getName() %></h3>
                        
                        <p><strong>Address:</strong> <%= garage.getAddress() %></p>
                        <p><strong>Contact:</strong> <a href="tel:<%= garage.getContactNo() %>"><%= garage.getContactNo() %></a></p>
                        
                        <% if (garage.getWebsite() != null && !garage.getWebsite().isEmpty()) { %>
                            <p><strong>Website:</strong> <a href="<%= garage.getWebsite() %>" target="_blank"><%= garage.getWebsite() %></a></p>
                        <% } %>
                        
                        <p><strong>Services:</strong> <%= garage.getServices() %></p>
                        <p><strong>Location:</strong>
                        	<a href="<%= garage.getField13() %>" target="_blank">View on Map</a>
                        </p>
                    </div>
                <% } %>
            <% } %>
        </div>

        <!-- Back Button -->
        <a href="userProfile.jsp" class="button">Back to Profile</a>
    </div>

</body>
</html>
