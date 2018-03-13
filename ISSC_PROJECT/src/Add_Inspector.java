

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


@WebServlet("/Add_Inspector")
public class Add_Inspector extends HttpServlet {
	Connection  conn;
	PreparedStatement ps,ps1;
	Statement st;
	RequestDispatcher rd;
	
    String inspector_id,inspector_name,inspector_address,inspector_phone,crop_name;
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
					//inspector_id=request.getParameter("inspector_id");
					inspector_name=request.getParameter("inspector_name");
					inspector_address=request.getParameter("inspector_address");
					inspector_phone=request.getParameter("inspector_phone");
					crop_name=request.getParameter("crop_name");
	    
					System.out.println("I_name:"+inspector_name+" add: "+inspector_address+" phone: "+inspector_phone+" crop:"+crop_name);
					
	    addInspector(request,response);
		}
		catch(Exception e){
			
		}
	}
	
	 void addInspector(HttpServletRequest request,HttpServletResponse response)throws SQLException,ServletException, IOException
	 {
		 PrintWriter out=response.getWriter();
		
		 try{
				Class.forName("com.mysql.jdbc.Driver");
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
				ps=conn.prepareStatement("insert into inspector values(?,?,?,?,?)");
				st=conn.createStatement();
				
				
				ResultSet rs=st.executeQuery("select * from inspector");
				
				if(rs.next()==false)
				{ System.out.println("No data in inspector");
				  ps.setInt(1, 1);
				}
				else{
					rs.last();
					ps.setInt(1, rs.getInt(1)+1);
				}
				
				ps.setString(2, inspector_name);
				ps.setString(3, inspector_address);
				ps.setString(4, inspector_phone);
				ps.setString(5, crop_name);
				
				
				if(ps.executeUpdate()>=1)
				{
					out.println("<script type='text/javascript'>window.alert('Details Added Successfully!!!');</script><center><font color='green'><b>Inspector Details Added Successfully</b></font></center>");
					rd=request.getRequestDispatcher("Add_Inspector.html");
					rd.include(request,response);
				}
				else{
					out.println("<font color='red'><b>Crop Details Not Added successfully</b></font>");
					rd=request.getRequestDispatcher("Add_Inspector.html");
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
