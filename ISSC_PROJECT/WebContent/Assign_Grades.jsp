<%@ page import="java.sql.*" language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

		
<%@ page import="cropTracking.sessionMgmt.CropSession" %>

<%!  
					ResultSet rs,rs1;
					Statement st,st1;
					Connection conn;
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

var x=4;

function getCount(temp)
{
	x=""+temp.id;
	x=x.slice(x.length-1,x.length);
	
}
	$(document).ready(function() {
		$("#div1").load("Navbar.html");
		
		$("textarea").keyup(function(){
			  $("#count"+x).text("Characters left: " + (250 - $(this).val().length));
			});
		
	});
	
	
	function continueYN(paramDate)
	{
		x=""+paramDate.id;
		
		var y=document.getElementById("description"+x).value;
		var z=document.getElementById("rating"+x).value;

		
		if(y==""||z=="")
			{
			return;
			}
		else
			{
			  if(z>10||z<0)
				  {
				  alert("Give Ratings Beetween 1-10"); return;
				  }
			}
		var c=confirm("Want To Assign Grades?");
		if(c==true)
		{
			document.getElementById("formAssignGrades"+x).action = "Assign_Grades";	
		}
		else
		{
			document.getElementById("formAssignGrades"+x).action ="Assign_Grades.jsp";
		}
	}
	
</script>

<div id='div1'></div>
<br>
<div class="container">
	<form class="form-inline" action="Assign_Grades.jsp" method="post">
		<div class="form-group">

			<label for="email">Select Crop</label>&nbsp&nbsp 
			<select class="form-control" id="sel1" name="list">

               <%  rs=st.executeQuery("select c_name from crop"); 
                   while(rs.next())
                   {
                	   out.println("<option value='"+rs.getString(1)+"'>"+rs.getString(1)+"</option>");
                   }
                   rs.close();
               %>
			</select>&nbsp&nbsp
			<button type='submit' class='btn btn-success'>Inspect Crop</button>
		</div>
	</form>
</div>


<%
    String crop=request.getParameter("list");
	String table = "<table class='table table-striped'><thead><tr><th>Case ID</th><th>Farmer Name</th><th>Crop Name</th><th>Assigned Inspector</th></tr></thead><tbody>";
	String output = "";  String c_id; String iname="";
	int flag = 0;
	
	try{
		
	if(crop!=null)
	{
    		 rs=st.executeQuery("select c_id from crop where c_name='"+crop+"'");
    		 if(rs.next())
    			 {
    			 c_id=rs.getString(1);
    		 	 rs.close();
    			
    		 	 rs = st.executeQuery("select case_id,farmer_id,c_id,end_date,inspector_id,gradeDescription from farmer_crop where end_date!='0000-00-00' and c_id='"+c_id+"'");
    			 }
    		 
    		 int id=0;
    		 while (rs.next()) 
    			{
    			  id++;
    			  rs1=st1.executeQuery("select i_name from inspector where i_id="+rs.getString(5));
    			  if(rs1.next())iname=rs1.getString(1);
    			  rs1.close();
    			  
    			  rs1=st1.executeQuery("select farmer_firstname,farmer_lastname from farmer where farmer_id="+rs.getString(2));
    			 
    			   if(rs.getString(6)==null &&rs.getString(5)!=null && rs1.next())
    			   {
    				   flag = 1;
    				   output+="<form action='#' id='formAssignGrades"+id+"' method='post'><input type='hidden' value='"+rs.getString(1)+"' name='case_id'>";
       				   output += "<tr><td>" + rs.getString(1) + "</td><td>" +rs1.getString(1)+" "+rs1.getString(2) + "</td><td>" + crop
       						+ "</td><td>"+iname+"</td></tr><tr><td style='padding-left:50px;padding-top:30px;'><font color='blue'>Enter Grades Here:<span class='badge' id='count"+id+"'></span></font></td><td><textarea rows='4' id='description"+id+"' onclick='getCount(this)' cols='40' name='grades' maxlength='250' required></textarea></td><td><font color='blue'>Give Rating</font><input type='number' id='rating"+id+"' name='rating' min='1' max='10' required></td><td><button type='submit' class='btn btn-info' id='"+id+"' style='margin-top:20px;' onclick='continueYN(this)'>Assign Grades</button></td></tr></form>";   
    			   }
    				rs1.close();
    		   } // end of while
    		 rs.close();
    }// end of if
	if ( flag==1)
		out.println("<div class='container'>"+table + output + "</tbody></table></div>");
	else {
			out.println("<div class='container'>");
			if(crop!=null)out.println("<center><font color='Red'><h3>There Are No Crop Information Found To Inspect</h3></font></center></div>");
		 }
	}// end of try
	
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		st.close(); st1.close();
	}
%>

</html>
