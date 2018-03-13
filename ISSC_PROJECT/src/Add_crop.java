

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cropTracking.sessionMgmt.CropSession;


@WebServlet("/Add_crop")
public class Add_crop extends HttpServlet {
	Connection  conn;
	PreparedStatement ps,ps1;
	Statement st;
	RequestDispatcher rd;
	ResultSet rs;
	
    String cropID,cropname,bestseason,bestmonth,water_req,soil,irrigation,avgyeild,temp,type;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*response.setHeader("Cache-Control","no-cache");
	    response.setHeader("Cache-Control","no-store");
	    response.setHeader("Pragma","no-cache");
	    response.setDateHeader ("Expires", 0);
	    response.setHeader("Expires","0"); 
	    response.setDateHeader("Expires",-1); 
	    */
		
		if(CropSession.isSet(request,response,2)==false)
		{
			response.sendRedirect("temp.jsp");
		}
		try{
		cropID=request.getParameter("cropID");
		cropname=request.getParameter("cropname");
		
		cropname=cropname.toUpperCase();
		
		bestseason=request.getParameter("bestseason");
		bestmonth=request.getParameter("bestmonth");
		water_req=request.getParameter("water_req");
		soil=request.getParameter("soil");
		irrigation=request.getParameter("irrigation");
	    avgyeild=request.getParameter("avgyeild");
	    temp=request.getParameter("temp");
	    type=request.getParameter("type");
	    
	    addCrop(request,response);
		}
		catch(Exception e){
			
		}
	}
	
	 void addCrop(HttpServletRequest request,HttpServletResponse response)throws SQLException,ServletException, IOException
	 {
		 PrintWriter out=response.getWriter();
		
		 try{
				Class.forName("com.mysql.jdbc.Driver");
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
				ps=conn.prepareStatement("insert into cropmaster values(?,?,?,?,?,?,?,?,?)");
				st=conn.createStatement();
				
				
				System.out.println("Crop Id: "+cropID);
				
			   rs=st.executeQuery("select crop_id from cropmaster where crop_id='"+cropID+"'");
				
			
				
				if(rs.next())
				{ 
				
				    System.out.println(" count : "+rs.getString(1));
					out.println("<center><font color='red'><b>Crop ID"+cropID+"Already Exists Enter Different cropID</b></font></center>");
					rd=request.getRequestDispatcher("Add_crop.html");
					rd.include(request,response);
				}
				rs.close();
				
				System.out.println();
				
				ps.setString(1,cropID);
				ps.setString(2, cropname);
				ps.setString(3, bestseason);
				ps.setString(4, bestmonth);
				ps.setString(5, water_req);
				ps.setString(6, soil);
				ps.setString(7, irrigation);
				ps.setString(8, avgyeild);
				ps.setString(9, temp);
				
				if(ps.executeUpdate()>=1)
				{
					rs=st.executeQuery("select c_id from crop where c_id='"+cropID+"'");
					if(!rs.next())
					{
					ps1=conn.prepareStatement("insert into crop values(?,?,?)");
					ps1.setString(1,cropID);
					ps1.setString(2, cropname);
					ps1.setString(3, type);
					
					if(ps1.executeUpdate()>=1)System.out.println("Added crop"+cropID);
					}
					out.println("<script>window.alert('!!! Crop Details Added Successfully !!!');</script><center><font color='green'><b>Crop Details Added Successfully</b></font></center>");
					rd=request.getRequestDispatcher("Add_crop.html");
					rd.include(request,response);
				}
				else{
					out.println("<font color='red'><b>Crop Details Not Added successfully</b></font>");
					rd=request.getRequestDispatcher("Add_crop.html");
					rd.include(request,response);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("SQL Exception");
			}
			finally{
				 ps.close(); conn.close();
			}
	 }
}
