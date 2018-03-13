<%@ page import="java.sql.*" language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
		
<%@ page import="cropTracking.sessionMgmt.CropSession,cropTracking.sessionMgmt.RemoveAdminSession" %>



<%!  
					ResultSet rs,rs1,rs2;
					Statement st,st1,st2;
					Connection conn;//Akola
					String city[]={"All","Ahmednagar","India","Akola","Amravati","Aurangabad","Beed","Bhandara","Buldana","Chandrapur","City","Dhule","Gadchiroli","Gondia","Hingoli","Jalgaon","Jalna","Kolhapur","Latur","Mumbai","Mumbai","Nagpur","Nanded","Nandurbar","Nashik","Osmanabad","Palghar","Parbhani","Pune","Raigad","Ratnagiri","Sangli","Sindhudurg","Suburban","Thane","Wardha","Washim","Yavatmal"};
					String from="",to="",region="";
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
	 st2 = conn.createStatement();
  }
  catch(Exception e)
  {
	  e.printStackTrace();
  }
  
  region=request.getParameter("Region");
  
%>



<html>


<script src="js\jquery.min.js"></script>
<script>

	$(document).ready(function() {
		$("#div1").load("Navbar.html");
	});
	
	  
	//document.getElementById("<%=request.getParameter("Region")%>").selected = "true";
</script>

<div id='div1'></div>
<br>
<div class="container">
	<form class="form-inline" action="ViewReport.jsp" method="post">
		<div class="form-group">

			<label >Select Area/Region</label>&nbsp&nbsp 
			<select class="form-control" id="sel" name="Region">

               <%  int i=0;
                
               out.println("<option>Select Region</option>");
                   while(i<36)
                   {
                	   if(region!=null)
                	   {
                		   if(region.equals(city[i])==true)
                		   {
                			   out.println("<option value='"+city[i]+"' id='"+city[i]+"' selected>"+city[i]+"</option>");  
                		   } 
                		   else
                		   {
                			   out.println("<option value='"+city[i]+"' id='"+city[i]+"'>"+city[i]+"</option>"); 
                		   }
                	   }
                	   else
                	   {
                		    out.println("<option value='"+city[i]+"' id='"+city[i]+"'>"+city[i]+"</option>");   
                	   }
                	   i++;
                   }
               %>
			</select>&nbsp&nbsp
			<label >From</label>&nbsp&nbsp<input type="date" class="form-control"  name="from" value="<%=request.getParameter("from") %>">&nbsp&nbsp
			<label >To</label>&nbsp&nbsp<input type="date" class="form-control"  name="to" value="<%=request.getParameter("to") %>" >&nbsp&nbsp
			<button type='submit' class='btn btn-success' >Get Details</button>
		</div>
	</form>
</div>

<div class="container">
<%

           
           
           from=request.getParameter("from");
           to=request.getParameter("to");
           //System.out.println("From: "+from.length()+" To: "+to.length()+" Region:"+region.length());

           
	String table = "<table class='table table-striped'><thead><tr><th>Crop Name</th><th>Average Yield In Ton</th></tr></thead><tbody>";
	String output = "";  String c_id; String iname="";
	int flag = 0;
	
	float avg=0.0f;
	
	String query="";
	if(region!=null)
	{
		    out.println(table);
		    
            rs=st.executeQuery("select c_id,c_name from crop order by c_id asc"); 
           
		     while(rs.next())
		     {
		    	 
		    	 if(region.equals("All")==false)
		    	 {
		    		 query="select farmer_crop.case_id,farmer_crop.farmer_id,farmer_crop.c_id from farmer_crop,farmer where farmer.farmer_id=farmer_crop.farmer_id and farmer.farmer_address='"+region+"' and farmer_crop.c_id='"+rs.getString(1)+"'";
		    	 }
		    	 else if(region.equals("All")==true)
		    	 {
		    		 query="select farmer_crop.case_id,farmer_crop.farmer_id,farmer_crop.c_id from farmer_crop where farmer_crop.c_id='"+rs.getString(1)+"'";
		    	 }
    		  rs1=st1.executeQuery(query);
    		  int count=0;
    		  while(rs1.next())
    		  {
    			  if(from.equals("")&&to.equals(""))rs2=st2.executeQuery("select quantity from yieldschedule where case_id="+rs1.getString(1));
    			 //select * from yieldschedule where harvest_date between '2018-03-01' and '2018-03-30';
    			  else if(from.equals("")==false && to.equals("")==false) rs2=st2.executeQuery("select quantity from yieldschedule where case_id="+rs1.getString(1)+" and harvest_date between '"+from+"' and '"+to+"'");
    			  else if(from.equals("")==false && to.equals("")==true) rs2=st2.executeQuery("select quantity from yieldschedule where case_id="+rs1.getString(1)+" and harvest_date >='"+from+"'");
    			  else if(from.equals("") && to.equals("")==false) rs2=st2.executeQuery("select quantity from yieldschedule where case_id="+rs1.getString(1)+" and harvest_date <='"+to+"'");
    			  
    			  while(rs2.next())
    			  {   count++;
    				  avg+=Float.parseFloat(rs2.getString(1));
    			  }
    			  rs2.close();
    			  
    		  }
    		  if(count!=0)out.println("<tr><td>"+rs.getString(2)+"</td><td>"+(avg/count)+"</td></tr>");
    		  else out.println("<tr><td>"+rs.getString(2)+"</td><td>"+0+"</td></tr>");
    		  rs1.close();
    		  
		     }
             rs.close();
	}
	
%>
</div><!-- End Of Table Div -->
</html>
