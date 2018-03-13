
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


@WebServlet("/Assign_Grades")
public class Assign_Grades extends HttpServlet {
	Connection  conn;
	Statement st;
	RequestDispatcher rd;
	
    String grades,case_id,rating;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 
		
		if(CropSession.isSet(request,response,2)==false)
		{
			response.sendRedirect("homePage.html");
		}
		try{
		grades=request.getParameter("grades");
		case_id=request.getParameter("case_id");
		rating=request.getParameter("rating");
		
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
				
				
				if(st.executeUpdate("update farmer_crop set gradeDescription='"+grades+"',rating="+rating+" where case_id="+case_id)>=1)
				{
					
					out.println("<script>window.alert('!!! Grades Assigned Successfully !!!');</script>");
					rd=request.getRequestDispatcher("Assign_Grades.jsp");
					rd.include(request,response);
				}
				else{
					out.println("<font color='red'><b>Grades Not Assigned successfully</b></font>");
					rd=request.getRequestDispatcher("Assign_Grades.jsp");
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

