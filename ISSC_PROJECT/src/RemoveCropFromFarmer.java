

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cropTracking.sessionMgmt.CropSession;



@WebServlet("/RemoveCropFromFarmer")
public class RemoveCropFromFarmer extends HttpServlet {
	
	String case_id;
	Connection conn;
	Statement st;
	RequestDispatcher rd;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
         
		
		case_id=request.getParameter("case_id");
		System.out.println(" !!!! Remove crop :"+case_id);
		
		try {
			removeCrop(request,response);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeCrop(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException, ClassNotFoundException, SQLException
	 {
		 	Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
			st=conn.createStatement();
			
			if(st.executeUpdate("delete from farmer_crop_update where case_id="+case_id)>=1)
			{
				
				if(st.executeUpdate("delete from farmer_crop where case_id="+case_id)>=1)
				{
					response.getWriter().println("<script type='text/javascript'>window.alert('Removed Crop Successfully !!!');</script>");
					rd=request.getRequestDispatcher("Farmer_Login_success.jsp");
					rd.include(request, response);
				}
			}
	 }
	
}
