<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="cropTracking.sessionMgmt.CropSession,cropTracking.sessionMgmt.*,java.sql.*" %>


<%!  
					ResultSet rs,rs1,rs2,rs3;
					Statement st,st1;
					Connection conn;
					String ename="",send,fid="",to="",name="";
					String msg=" Good Day :) Please Update Your Crop Status, Thank You !!!";
					String table = "<table class='table table-striped'><thead><tr><th>Case ID</th><th>Event Name</th><th>Start Date</th></tr></thead><tbody>";
%>

<%


if(CropSession.isSet(request,response,2)==false)
{
	response.sendRedirect("homePage.html");
}



  try{
	Class.forName("com.mysql.jdbc.Driver");
	 conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking", "root", "root");
	 st = conn.createStatement();
	 st1 = conn.createStatement();
  }
  catch(Exception e)
  {
	  e.printStackTrace();
  }
%>


<html>


<script src="js\jquery.min.js"></script>
<script>

	$(document).ready(function() {
		$("#div1").load("Navbar.html");
	});
	
	
</script>

<div id='div1'></div>
<br>
<div class="container">
 <center>
	<form class="form-inline" action="#" method="post">
		<div class="form-group">
			<button type='submit' name="notify" value="Send Notification" class='btn btn-success'>Send Notification</button>
		</div>
	</form>
  </center>
</div>


<div class="container">
<%

java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());


 send=request.getParameter("notify");

rs=st.executeQuery("select case_id,e_id,event_start_date from farmer_crop_update where event_end_date is null and event_start_date <='"+date+"'");

out.println(table);
while(rs.next())
{
	
	rs1=st1.executeQuery("select e_name from event where e_id='"+rs.getString(2)+"'");
	if(rs1.next())
		{
		  ename=rs1.getString(1); rs1.close();
		}
	
	if(send!=null)
	{
		rs2=st1.executeQuery("select farmer_id from farmer_crop where case_id="+rs.getString(1));
		if(rs2.next())
		{
			System.out.println("!!! Inside 1 !!!");
		  fid=rs2.getString(1); rs2.close();
		}
		rs3=st1.executeQuery("select farmer_mail_id,farmer_firstname from farmer where farmer_id="+fid);
		
		if(rs3.next())
		{
		  System.out.println("!!! Inside 2 !!!");
		  to=rs3.getString(1); name=rs3.getString(2); rs3.close();
		}
		
		System.out.println("!! To: "+to+" name: "+name);
		SendNotification.send("pritam.aug31@gmail.com","Pulsar150",to,"Crop Tracking and Information","Hello "+name+""+msg);    
	}
	
	out.println("<tr><td>"+rs.getString(1)+"</td><td>"+ename+"</td><td>"+rs.getString(3)+"</td><tr>");
	
}

out.println("</tbody></table>");
%>

</div>




