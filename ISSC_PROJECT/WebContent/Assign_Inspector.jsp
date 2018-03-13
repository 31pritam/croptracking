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



	$(document).ready(function() {
		$("#div1").load("Navbar.html");
	});
	
	
	function continueYN(temp)
	{
		var x=x=""+temp.id;
		var c=confirm("Want To Assign Inspector?");
		var y=document.getElementById("inspector"+x).value;  
		
		if(y=="")
			{
			return;
			}
		
		
		if(c==true)
		{
			document.getElementById("formAssignInspector"+x).action = "Assign_Inspector";	
		}
		else
		{    
			//document.getElementById("formAssignInspector"+x).action ="Assign_Inspector.jsp";
		}
	}
	
</script>

<div id='div1'></div>
<br>
<div class="container">
	<form class="form-inline" action="Assign_Inspector.jsp" method="post">
		<div class="form-group">

			<label for="email">Select Crop</label>&nbsp&nbsp 
			<select class="form-control" id="sel" name="list">

               <%  rs=st.executeQuery("select c_name from crop"); 
                   while(rs.next())
                   {
                	   out.println("<option value='"+rs.getString(1)+"'>"+rs.getString(1)+"</option>");
                   }
                   rs.close();
               %>
			</select>&nbsp&nbsp
			<button type='submit' class='btn btn-success'>Check Crop</button>
		</div>
	</form>
</div>


<%
    String crop=request.getParameter("list");
	String table = "<table class='table table-striped'><thead><tr><th>Case ID</th><th>Farmer Name</th><th>Crop Name</th><th>Select Inspector</th></tr></thead><tbody>";
	String output = "";  String c_id; String iname="";
	int flag = 0;
	
	try{
		
	if(crop!=null)
	{
		
          rs=st.executeQuery("select i_id,i_name from inspector where crop_name='"+crop+"'"); 
            while(rs.next())
            {
         	   iname+="<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";
            }
            rs.close();
		
    		 rs=st.executeQuery("select c_id from crop where c_name='"+crop+"'");
    		 if(rs.next())
    			 {
    			 c_id=rs.getString(1);
    		 	 rs.close();
    			
    		 	 rs = st.executeQuery("select case_id,farmer_id,c_id,end_date,inspector_id from farmer_crop where end_date!='0000-00-00' and c_id='"+c_id+"'");
    			 }
    		 	 
    		 int id=0;
    		 while (rs.next()) 
    			{
    			  id++;
    			  rs1=st1.executeQuery("select farmer_firstname,farmer_lastname from farmer where farmer_id="+rs.getString(2));
    			 
    			   if(rs.getString(5)==null && rs1.next())
    			   {
    				   flag = 1;
    				   output+="<form id='formAssignInspector"+id+"' method='post'><input type='hidden' value='"+rs.getString(1)+"' name='case_id'>";
       				   output += "<tr><td>" + rs.getString(1) + "</td><td>" +rs1.getString(1)+" "+rs1.getString(2) + "</td><td>" + crop
       						+ "</td><td>"+"<select class='form-control' id='inspector"+id+"' name='i_id' required>"+iname+"</select></td><td><button type='submit' class='btn btn-info' id="+id+" onclick='continueYN(this)'>Assign Inspector</button></td></tr></form>";   
    			   }
    				rs1.close();
    		   } // end of while
    		 rs.close();
    }// end of if
	if ( flag==1)
		out.println("<div class='container'>"+table + output + "</tbody></table></div>");
	else {
			out.println("<div class='container'>");
			if(crop!=null)out.println("<center><font color='Red'><h3>There Are No Such Crop Information Available</h3></font></center></div>");
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
