<%@ page import="java.sql.*"%>

<%@ page import="cropTracking.sessionMgmt.CropSession" %>



<% 

if(CropSession.isSet(request,response,1)==false)
{   out.println("<script>document.getElementById('formCrop').style.display = 'none';</script>");
    out.println("<script>history.forward();</script>");
	response.sendRedirect("homePage.html");
}

String username = (String)session.getAttribute("username");
System.out.println("!!!!!Farmer_Login_success username: "+username);
				

%>




<%
            Class.forName("com.mysql.jdbc.Driver");

			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
			System.out.println("Connection successfull");
            Statement st=conn.createStatement();
            Statement st1=conn.createStatement(); Statement st2=conn.createStatement();
            
 %>

<%! ResultSetMetaData rsmd; %>

<html>
<head>
<title>Crop Tracking</title>
<meta charset="utf-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="CSS\css\bootstrap.min.css">
<script src="js\jquery.min.js"></script>
<script src="js\bootstrap.min.js"></script>
</head>




<%

if(request.getParameter("ued")!=null)
{
	out.println("<input type='hidden' id='show' value='yes'>");
}

if(request.getParameter("uhd")!=null)
{
	out.println("<input type='hidden' id='show1' value='yes1'>");
}

%>


<script>

$(document).ready(function(){ 

	if ($("#show1").val() == 'yes1') {
		$("#Tag").load("Harvest_Schedule.jsp");
        $("#Event").hide();
        $("#show1").hide();
	}
	if ($("#show").val() == 'yes') {
		$("#Tag").load("Farmer_Crop_Update.jsp");
        $("#Event").hide();
        $("#show").hide();
	}
	
});



var ca;
function myFunc(crop) {
    var x=""+ crop.id;
    ca = x.split(' '); 
    var tmp;
    var output="";
    
    document.getElementById("Event").style.display = "none";
   // document.getElementById("Event").style.visibility = "hidden";
   // window.alert(document.getElementById(ca[8]).value);
    
       ////1 12  (length-2)/2
        for(var n = 1,nd=n+8; n <=(ca.length-2)/2 ; n++,nd++) 
    	{
    	  tmp="<tr><td>"+ca[n]+"</td><td>"+ca[nd]+"</td></tr>"; 
    	  output=output+tmp;    
    	}                                                                                 //style='background-color: #707B7C;
    	document.getElementById("Tag").innerHTML="<Font color='red'><center><h1 style='background-color:lightblue ;'>"+ca[8]+" DETAILS</h1><form action='SelectedCrop' class='form-inline' method='post'><input type='hidden' name='cropRadio' value="+ca[8]+"><button type='submit' class='btn btn-success' >Add Crop</button></form></center></font>"+"<table class='table table-striped'><tbody>"+output+"</tbody><tr><td colspan='2'><center><button type='button' class='btn btn-info' onclick='viewEvent()'>View Event</button></center></td></tr></table>";   
}

function viewEvent()
{     document.getElementById("Event").style.display = "block";
	//document.getElementById("Event").style.visibility = "visible";
	document.getElementById("Event").innerHTML=" ";
	var event=document.getElementById(ca[8]).value;

    var e = event.split(' ');
    var tmp;
    var output="";
    
    var l=Math.ceil((e.length-2)/2);
    
   // window.alert(e+" e.length="+(e.length-2)+" e[l]="+e[l]+" l="+l);
   
    for(var n = 0,nd=l ; n <=(e.length-2)/2 ; n++,nd++) 
    	{
    	  tmp="<tr><td>"+e[n]+"</td><td>"+e[nd]+"</td></tr>"; 
    	  output=output+tmp;    
    	}                                                                                 //style='background-color: #707B7C;
    	document.getElementById("Event").innerHTML="<Font color='red'><center><h1 style='background-color:lightblue ;'>"+ca[8]+" EVENT DETAILS</h1></center></font>"+"<table class='table table-striped'><thead><tr><th>Event</th><th>No of Days</th></tr></thead><tbody>"+output+"</tbody></table>";   
}


$(document).ready(function(){
	
    $("#update_crop").click(function(){
        $("#Tag").load("Farmer_Crop_Update.jsp");
        $("#Event").hide();
    });
    $("#harvest").click(function(){
        $("#Tag").load("Harvest_Schedule.jsp");
        $("#Event").hide();
    });
});

function continueYN1()
{
	var x = document.forms["selectCropForm"]["cropRadio"].value;
    if (x == "") {
        alert(" !!! No Crop Is Selected, Please Select The Crop First !!!");
        document.getElementById("formCrop").action ="Farmer_Login_success.jsp";
        return;
    }
	
	var c=confirm("Want To Select Crop?");
	if(c==true)
	{
		document.getElementById("formCrop").action = "SelectedCrop";	
	}
	else
	{
		document.getElementById("formCrop").action ="Farmer_Login_success.jsp";
	}
}



</script>

<body>
	<form name="selectCropForm" action="#" method='post' id='formCrop'>

		<div>

			<nav class="navbar navbar-inverse">
				<div class="container-fluid">
					<div class="navbar-header">
						<img src="IMAGES\aaa.jpg" style="width: 50px; margin-top:10px;height: 30px">
					</div>
					<ul class="nav navbar-nav">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">View Crop<span class="caret"></span></a>
							<ul class="dropdown-menu">

								<% 
            ResultSet rs=st.executeQuery("select crop_name, best_season,best_month,water_requirement,soil,irrigation,average_yield,temperature,crop_id from cropmaster");
            ResultSet rs1,rs2;//=st1.executeQuery("select c_name, c_id from crop");
            String str1="",str2="",str3="",str4="";
            
            rsmd=rs.getMetaData(); 
           /* if(rs.next())
            {
            	 
            }*/
            
           
            while(rs.next())
            { 
              // rs1=st1.executeQuery("select");
               rs1=st1.executeQuery("select e_name from event where e_id in (select e_id from crop_event where event.e_id=crop_event.e_id and '"+rs.getString(9)+"'=crop_event.c_id)");
               rs2=st2.executeQuery("select e_id,no_of_days from crop_event where crop_event.c_id='"+rs.getString(9)+"'");
               
               for(int j=1;j<=8;j++ )str1=str1+rsmd.getColumnName(j)+" ";
               
               for(int i=1; i<=8;i++)str2=str2+rs.getString(i)+" "; 
               
               while(rs1.next())str3=str3+rs1.getString(1)+" "; 
               
               while(rs2.next())str4=str4+rs2.getString(2)+" ";
                        
               System.out.println(str3+"  "+str4); 
              
               System.out.println(rs.getString(1)); 
               out.println("<input type='hidden' id='"+rs.getString(1)+"' value='"+str3+str4+"'>");
               
               out.println("<li><a href='#' id='"+str1+str2+"'onclick='myFunc(this)' >"+rs.getString(1)+"</a></li>");
                str1=""; str2="";  str3=""; str4="";   
                       
            }
            
           // out.println("<li><center><input type='submit' name='crop' value='Select Crop' style='background-color:#f7f8f9;width:150px;color:black;border:none;' onclick='continueYN1()'></center></li>");
            %>

							</ul></li>
						<li><a href="#" id='update_crop'>Update Status</a></li>
						<li><a href="#" id='harvest'>Harvest Schedule</a></li>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li><a href="temp.jsp"> Log-Out </a></li>
					</ul>
				</div>
			</nav>
		</div>
		<input type='hidden' name='username'
			value='<%=username %>'>
	</form>
	<div class="container" id="Tag">
		<Font color='red'><center>
				<h1 style='background-color: lightblue;'>Visit View Crop To See
					Available Crop Details</h1>
			</center></Font>
		<div class="container" id="img"></div>
	</div>

	<div class='container' id="Event"></div>

</body>

</html>