<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.sql.*"%>
	
<%@ page import="cropTracking.sessionMgmt.CropSession" %>
	
<%!
    ResultSet rs,rs1,rs2,rs3; 
    Statement st,st1,st2,st3;
	Connection conn;

    String username="",fid="",sd="";
    
         
%>

<script>

var flag2; var dt2;
function getConfirmation2()
{
	if(flag2==0)return;
	
	var c=confirm("Want To Update Harvest Schedule?");
	if(c==true)
	{
		document.getElementById("harvestSchedule"+dt2).action = "update_harvest_date";	
	}
	else
	{
		//return;
		document.getElementById("harvestSchedule"+dt2).action ="Farmer_Login_success.jsp?uhd=@!#";
	}
}

function continueYN2(paramDate)
{
	 var d=new Date();
	 
	 var x=""+paramDate.id;
	 dt2 = x.slice(x.length-1,x.length);
	 x=document.getElementById(x+dt2).value;
	 var y=document.getElementById("quantity"+dt2).value;
	 
	   
	 if(x==""||y=="")
		 {
		  return;
		 }
	 
	 flag2=1;
	    var text;		
		if (x != "") {
	    	 		  text=x.split("-");
	    		 	  if(text[0]<d.getFullYear())flag2=0;
	    	   		  else if((text[1]-1)<d.getMonth())flag2=0;
	    	    	  else if(text[2]<d.getDate() && (text[1]-1)<=d.getMonth())flag2=0;
	    	    
	    	    if(flag2==0)
	    	    	{
	    	    	    alert("!!! You Can Not Select Past date !!!");
	    	    	    document.getElementById("harvestSchedule"+dt2).action ="Farmer_Login_success.jsp?uhd=@!#";
	    	    	   // document.getElementById("demo").innerHTML="You can not select past date!!";

	    	    	}
	    	    else if(flag2==1)
	    	    	{
	    	    	  getConfirmation2();
	    	    	}
	    }   
}

</script>

<%      


if(CropSession.isSet(request,response,1)==false)
{
	out.println("<script>document.getElementById('formCrop').style.display = 'none';</script>");
	response.sendRedirect("homePage.html");
}

String username = (String)session.getAttribute("username");
String fid=(String)session.getAttribute("farmerID");
	
	
String table = "<table class='table table-striped'><thead><tr><th>Crop Selection Date</th><th>Crop Name</th><th>Quantity Of Yield</th><th>Harvest Date</th></thead><tbody></table>";


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

//case_id | farmer_id | c_id | start_date
rs=st.executeQuery("select case_id,farmer_id,c_id,start_date from farmer_crop where farmer_id="+fid+" and gradeDescription is not NULL and ready_to_harvest is NULL");
int flag=1,id=1;

String output="";

while(rs.next())
{
	flag=0;
	rs1=st1.executeQuery("select c_name from crop where c_id='"+rs.getString(3)+"'");
	if(rs1.next()){}
	output+="<form action='#' id='harvestSchedule"+id+"' method='post'><input type='hidden' name='case_id' value='"+rs.getString(1)+"'>"+"<table class='table table-striped'><thead><tr><th>Crop Selection Date</th><th>Crop Name</th><th>Quantity Of Yield</th><th>Harvest Date</th></thead><tbody><tr><td>"+rs.getString(4)+"</td><td>"+rs1.getString(1)+"</td><td><input type='text' name='quantity' id='quantity"+id+"' placeholder='Enter Quantity here' required></td><td><input type='date' id='harvest_date"+id+""+id+"' name='HarvestDate' required></td><td><button type='submit'  class='btn btn-info' id='harvest_date"+id+"' onclick='continueYN2(this)'>Harvest</button></td></tr></tbody></table></form>";

	rs1.close();
	id++;
}
rs.close(); st.close(); st1.close(); conn.close();
if(flag==0)
{
	out.println("<div class='container'>"+output+"</div>");
}
else if(flag==1)
{
	out.println("<Font color='red'><center><h1 style='background-color: lightblue;'> No Crop Information Found!!!</h1></center></Font>");
}

 %>
 
 
 <div class="modal fade" id="myModal" role="dialog"><!-- data-toggle='modal' data-target='#myModal' -->
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        
        <div class="modal-body">
          <p id="demo"></p>
        </div>
        
      </div>
    </div>
  </div>
 
 
 
 
 
 
 
 