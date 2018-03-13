

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

@WebServlet("/update_end_date")
public class update_end_date extends HttpServlet {

	Connection conn;
	Statement st;
	ResultSet rs;
	RequestDispatcher rd;
	String date,case_id,e_id;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//update_end_date?end_date=&case_id=1&e_id=e2
		
		
		/*response.setHeader("Cache-Control","no-cache");
	    response.setHeader("Cache-Control","no-store");
	    response.setHeader("Pragma","no-cache");
	    response.setDateHeader ("Expires", 0);
	    response.setHeader("Expires","0"); 
	    response.setDateHeader("Expires",-1); 
	    */
		if(CropSession.isSet(request,response,1)==false)
		{    
		   response.sendRedirect("homePage.html");
		}
         
		date=request.getParameter("end_date");
		case_id=request.getParameter("case_id");
		e_id=request.getParameter("e_id");	
		
		
		try {
			updateDate(request,response);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	 public void updateDate(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException, ClassNotFoundException, SQLException
	 {
		 	Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
			st=conn.createStatement();
			
			//update farmer_crop_update set event_end_date="2018-2-22" where case_id=1 and e_id='e1';
			
			
			
			if(st.executeUpdate("update farmer_crop_update set event_end_date='"+date+"',status='complete' where case_id="+case_id+" and e_id='"+e_id+"'")>=1)
			{
				rs=st.executeQuery("select count(*) from farmer_crop_update where case_id="+case_id+" and event_end_date is NULL");
				if(rs.next())if(rs.getInt(1)==0)
				{
					int i=st.executeUpdate("update farmer_crop set end_date='"+date+"' where case_id="+case_id);
					if(i==1)
					{
						response.getWriter().println("<script type='text/javascript'>window.alert('You Completed All Events Successfully !!!');</script><input type='hidden' id='show' value='yes'>");
						
						
						rd=request.getRequestDispatcher("Farmer_Login_success.jsp");
						rd.include(request, response);
					}
				}
				else
				{
					response.getWriter().println("<script type='text/javascript'>window.alert('Updated Successfully !!!');</script><input type='hidden' id='show' value='yes'>");
					rd=request.getRequestDispatcher("Farmer_Login_success.jsp");
					rd.include(request, response);	
				}
				
			}	
	 
	 }
	
}
