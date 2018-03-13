</body>
</html>
<!DOCTYPE html>
<html>
<head>
<title>Crop Tracking</title>
<script type="text/javascript">
history.forward();
</script>

</head>


<link rel="stylesheet" href="CSS\css\bootstrap.min.css">
<script src="js\jquery.min.js"></script>
<script src="js\bootstrap.min.js"></script>

<%

if(request.getParameter("lg")!=null)
{
session=request.getSession();
session.removeAttribute("admin");
session.invalidate();
response.sendRedirect("homePage.html");
}

%>

<body background="IMAGES\background1.jpg">

	<center>
		<div>
			<font color="yellow"><h1>Crop Tracking And Information</h1></font>
		</div>
			<div class="container"
				style="margin-left: 25%; margin-top: 8%; width: 50%;">
				<form method="post" action="ValidateLogin">
					<font color="gray">
						<p class="h4 text-center mb-4">You Are Going To Logout</p>
						<hr> 
						Do You Wan't To Logout? <a href="temp.jsp?lg=true">Yes!&nbsp</a><a href="Admin_Login_Success.html">No!</a>				
					</font>
				</form>
			</div>
		
	</center>

</body>
</html>



