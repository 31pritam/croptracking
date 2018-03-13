
<%@ page import="java.io.*, java.sql.*" language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="cropTracking.sessionMgmt.CropSession" %>

<script>
var flag; var dt;
function getConfirmation()
{
	if(flag==0)return;
	
	var c=confirm("Want To Update Date?");
	if(c==true)
	{
		document.getElementById("formCropUpdate"+dt).action = "update_end_date";	
	}
	else
	{
		document.getElementById("formCropUpdate"+dt).action ="Farmer_Login_success.jsp?ued=!@#";
	}
}


function DeleteCropConfirmation(paramDate)
{
	var x=""+paramDate.id;
	var c=confirm("Want To Remove Crop?");
	if(c==true)
	{
		document.getElementById("removeDateForm"+x).action = "RemoveCropFromFarmer";	
	}
	else
	{
		document.getElementById("removeDateForm"+x).action ="Farmer_Login_success.jsp?ued=!@#";
	}
}


function continueYN(paramDate)
{
	 var d=new Date();
	 flag=1;
	 var x=""+paramDate.id;
	 dt = x.slice(x.length-1,x.length);
	 x=document.getElementById(x+dt).value;
	 	
	    var text;		
		if (x != "") {
	    	 		  text=x.split("-");
	    		 	  if(text[0]>d.getFullYear())flag=0;
	    	   		  else if((text[1]-1)>d.getMonth())flag=0;
	    	    	  else if(text[2]>d.getDate()&&(text[1]-1)>=d.getMonth())flag=0;
	    	    
	    	    if(flag==0)
	    	    	{
	    	    	    document.getElementById("formCropUpdate"+dt).action ="Farmer_Login_success.jsp?ued=!@#";
	    	    		alert("!!! You Can Not Select Future date !!!"); return;
	    	    	}
	    	    else if(flag==1)
	    	    	{
	    	    	  getConfirmation();
	    	    	}
	    }   
}


</script>
<input type='hidden' id='show' value='yes'>
<%!
    ResultSet rs,rs1,rs2,rs3; Statement st,st1,st2,st3;
	Connection conn;
	
	String output="";
    String username="",farmerID="",sd="",ed="";
    
    String table="<table class='table table-striped'><thead><tr><th>Event</th><th>Start Date</th><th>End Date</th></tr></thead><tbody>";
    
    
    %>



<%

    try {
	      
    	if(CropSession.isSet(request,response,1)==false)
    	{
    		out.println("<script>document.getElementById('formCrop').style.display = 'none';</script>");
    		response.sendRedirect("homePage.html");
    	}

    	
    	String username = (String)session.getAttribute("username");
		
        Class.forName("com.mysql.jdbc.Driver");
		conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
		st=conn.createStatement();
		st1=conn.createStatement();
		st2=conn.createStatement();
		st3=conn.createStatement();
		
		rs=st.executeQuery("select farmer_id from farmer where user_name='"+username+"'");
		if(rs.next()){
			farmerID=rs.getString(1);
			System.out.println("farmer ID: "+farmerID);
		}
		
		rs.close();
		
		rs1=st.executeQuery("select case_id,c_id,start_date from farmer_crop where farmer_id="+farmerID+" and end_date='0000-00-00'");
				String caseid=null,eid=null;
				int otpt=1; int id=0;
		while(rs1.next())
		{   id++;
			otpt=0;
			System.out.println("case_id: "+rs1.getString(1)+" c_id: "+rs1.getString(2));
			caseid=rs1.getString(1);
		
			
			rs=st2.executeQuery("select c_name from crop where c_id='"+rs1.getString(2)+"'");
			
			
			if(rs.next())
				{
				out.println("<form action='Farmer_Login_success.jsp' id='removeDateForm"+id+"' method='post'>");
				out.println("<Font color='#85929E'><h1 style='background-color:lightblue;'>"+rs1.getString(3)+"</font>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<Font color='red'>"+rs.getString(1)+"</font>");
				out.println("<button type='submit' class='btn btn-success' id='"+id+"' onclick='DeleteCropConfirmation(this)' style='alignment:right;'>Remove Crop</button></h1>");
				out.println("<input type='hidden' name='case_id' value='"+caseid+"'></form>");
				}
			
			
			rs2=st1.executeQuery("select e_id,event_start_date,event_end_date from farmer_crop_update where case_id="+rs1.getString(1));
			
			int flag=1; 
			while(rs2.next())
			{   
			
				rs3=st3.executeQuery("select e_name from event where e_id='"+rs2.getString(1)+"'");
				
				 ed=rs2.getString(3);
				if(ed!=null){
					ed="<input type='text' value='"+rs2.getString(3)+"' readonly>";
				}
				 
				sd="<input type='text' value='"+rs2.getString(2)+"' readonly>"; 
				if(ed==null)
				{
						if(flag==1)
							{
							   eid=rs2.getString(1);
								ed="<input type='date' name='end_date' id='end_date"+id+""+id+"' required min='"+rs2.getString(2)+"'>&nbsp;&nbsp;<button type='submit' id='end_date"+id+"' class='btn btn-info' onclick='continueYN(this)'>Update</button>";
								flag=0;	
							}
						else
							{
								ed="";
							}
				  }
 				if(rs3.next())output=output+"<tr><td>"+rs3.getString(1)+"</td><td>"+sd+"</td><td>"+ed+"</td></tr>"; 
 				rs3.close();
     	  	 }
		     
		 	// out.println(table+output+"</tbody></table>");
		 	//update_end_date 
		 	 out.println("<form id='formCropUpdate"+id+"' method='post'>"+table+output+"</tbody></table><input type='hidden' name='case_id' value='"+caseid+"'><input type='hidden' name='e_id' value='"+eid+"'></form>");
		 	 
		     rs2.close(); output=""; rs.close();
		}
		if(otpt==1)
		{
			out.println("<Font color='red'><center><h1 style='background-color: lightblue;'>You Have Not Selected Any Crop!!!</h1></center></Font>");
		}
		rs1.close();       
	} 
	 
	catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    finally{
    	conn.close();
    	//rs.close(); rs1.close(); rs2.close(); rs3.close();
    	st.close(); st1.close(); st2.close(); st3.close();
    }
 %>