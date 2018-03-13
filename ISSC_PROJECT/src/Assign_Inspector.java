
import java.io.IOException;
import java.io.PrintWriter;
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


@WebServlet("/Assign_Inspector")
public class Assign_Inspector extends HttpServlet {
	Connection  conn;
	Statement st;
	RequestDispatcher rd;
	
    String i_id,case_id;
    
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
			response.sendRedirect("homePage.html");
		}
		
		try{
		i_id=request.getParameter("i_id");
		case_id=request.getParameter("case_id");
		
		
	    assignInspector(request,response);
		}
		catch(Exception e){
			
		}
	}
	
	 void assignInspector(HttpServletRequest request,HttpServletResponse response)throws SQLException,ServletException, IOException
	 {
		 PrintWriter out=response.getWriter();
		
		 try{
				Class.forName("com.mysql.jdbc.Driver");
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
				
				st=conn.createStatement();
				
				
				if(st.executeUpdate("update farmer_crop set inspector_id="+i_id+" where case_id="+case_id)>=1)
				{
					
					out.println("<script>window.alert('!!! Inspector Assigned Successfully !!!');</script>");
					rd=request.getRequestDispatcher("Assign_Inspector.jsp");
					rd.include(request,response);
				}
				else{
					out.println("<font color='red'><b>Inspector Not Assigned successfully</b></font>");
					rd=request.getRequestDispatcher("Assign_Inspector.jsp");
					rd.include(request,response);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("SQL Exception");
			}
			finally{
				st.close(); conn.close();
			}
	 }
}

