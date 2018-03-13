

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

/**
 * Servlet implementation class update_harvest_date
 */
@WebServlet("/update_harvest_date")
public class update_harvest_date extends HttpServlet {
	Connection conn;
	Statement st;
	RequestDispatcher rd;
	String date,quantity,case_id;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
         
		date=request.getParameter("HarvestDate");
		quantity=request.getParameter("quantity");
		case_id=request.getParameter("case_id");
		

		try {
			updateHarvestDate(request, response);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateHarvestDate(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException, ClassNotFoundException, SQLException
	 {
		
		Class.forName("com.mysql.jdbc.Driver");
		conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
		st=conn.createStatement();
		
		try
		{
		int i=st.executeUpdate("insert into yieldschedule(case_id,harvest_date,quantity) values("+case_id+",'"+date+"','"+quantity+"')");
	    if(i>=1)
	    {
	    	i=st.executeUpdate("update farmer_crop set ready_to_harvest='yes' where case_id="+case_id);
	    	response.getWriter().println("<script type='text/javascript'>window.alert('Updated Successfully !!!');</script><input type='hidden' id='show1' value='yes1'>");
			rd=request.getRequestDispatcher("Farmer_Login_success.jsp");
			rd.include(request, response);	
	    }
	 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			st.close(); conn.close();
		}
	 }
	 
}










