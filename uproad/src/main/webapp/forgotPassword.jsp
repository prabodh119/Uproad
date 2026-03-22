<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Forgot Password</title>
<style type="text/css">
	body {
		font-family: Arial, sans-serif;
		background: #f4f4f4;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100vh;
	}
	
	.container {
		background: white;
		padding: 20px;
		border-radius: 8px;
		width: 300px;
		text-align: center;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	}
	
	input {
		width: 100%;
		padding: 10px;
		margin: 10px 0;
		border: 1px solid #ccc;
		border-radius: 4px;
		box-sizing: border-box;
	}
	
	button {
		width: 100%;
		padding: 10px;
		background-color: #4CAF50;
		color: white;
		border: none;
		border-radius: 4px;
		cursor: pointer;
	}
	button:hover {
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
</style>
</head>
<body>
	<div class="container">
		<h2>Forgot Password</h2>
		<form action="/ForgotPasswordServlet" method="POST">
			<input type="email" name="email" placeholder="Enter your email"
				required />
			<button type="submit">Send Reset Link</button>
		</form>
		<a class="signup-link" href="login.jsp">Back to Login</a>
	</div>
</body>
</html>