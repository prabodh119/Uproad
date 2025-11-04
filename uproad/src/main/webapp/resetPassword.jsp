<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="dbc.DBConnection"%>
<%@ page import="java.io.*"%>
<%@ page import="javax.servlet.*"%>

<%
String token = request.getParameter("token");
DBConnection dbc = new DBConnection();
boolean valid = dbc.isValidResetToken(token);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reset Password</title>
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

    .container {
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        width: 100%;
        max-width: 300px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        text-align: center;
    }

    h2, h3 {
        color: #333;
        margin-bottom: 20px;
    }

    input[type="password"] {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
        font-size: 14px;
    }

    input[type="password"]:focus {
        border-color: #1976d2;
        outline: none;
    }

    button {
        width: 100%;
        padding: 12px;
        background-color: #4CAF50;
        color: white;
        font-size: 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    button:hover {
        background-color: #45a049;
    }

    .message {
        margin-top: 15px;
        font-size: 14px;
        color: #555;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }
</style>
</head>
<body>
    <div class="container">
        <%
        if (valid) {
        %>
        <h2>Reset Your Password</h2>
        <form action="/uproad/ResetPasswordServlet" method="POST">
            <input type="hidden" name="token" value="<%=token%>" /> 
            <input type="password" name="password" placeholder="New Password" required>
            <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
            <button type="submit">Update Password</button>
        </form>
        <%
        } else {
        %>
        <h3>Invalid or expired reset link</h3>
        <p class="message">Please request a new password reset from the login page.</p>
        <a href="login.jsp">
            <button type="button">Go to Login</button>
        </a>
        <%
        }
        %>
    </div>
</body>
</html>