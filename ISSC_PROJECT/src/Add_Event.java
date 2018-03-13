



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


@WebServlet("/Add_Event")
public class Add_Event extends HttpServlet {
	Connection  conn;
	PreparedStatement ps;
	Statement st;
	RequestDispatcher rd;
	
    String cropID,eventname,no_of_days;
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
		eventname=request.getParameter("eventName");
		no_of_days=request.getParameter("no_of_days");
		
		eventname.toUpperCase();
		
		System.out.println(eventname);
		
		eventname=eventname.toUpperCase();
		
	    System.out.println(eventname);
		
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
				ps=conn.prepareStatement("insert into crop_event values(?,?,?)");
				st=conn.createStatement();
				
				ResultSet rs=st.executeQuery("select e_id from event where e_name='"+eventname+"'");
				
				
				ps.setString(1,cropID);
				if(rs.next())ps.setString(2, rs.getString(1));
				else{
					  System.out.println("Error ");
				    }
				ps.setInt(3, Integer.parseInt(no_of_days));
				
				
				if(ps.executeUpdate()>=1)
				{
					
					out.println("<script>windw.alert('!!! Crop_Event Details Added Successfully !!!');</script><center><font color='green'><b>Crop_Event Details Added Successfully</b></font><br><a href='Admin_Login_Success.html'>Admin_home</a></center>");
					rd=request.getRequestDispatcher("Add_crop.html");
					rd.include(request,response);
				}
				else{
					out.println("<font color='red'><b>Crop_Event Details Not Added successfully</b></font>");
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

