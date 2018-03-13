import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cropTracking.sessionMgmt.CropSession;
@WebServlet("/SelectedCrop")
public class SelectedCrop extends HttpServlet {
	
	ResultSet rs,rs1,rs2;
	RequestDispatcher rd;
	String username;
	
	
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
         HttpSession session = request.getSession();
		 username = (String)session.getAttribute("username");

		    String crop[] = request.getParameterValues("cropRadio");
		    
		try {
					
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());

			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
			Statement st=conn.createStatement(); 
			Statement st1=conn.createStatement();
			Statement st2=conn.createStatement();
			Statement st3=conn.createStatement();
			//out.println(request.getParameter("username"));

			System.out.println(" selectCrop::: username: "+username);
             
			int i;
			 rs=st.executeQuery("select * from farmer_crop");
			 if(rs.next()==false){
				 i=1;
			 }
			 else{
				 rs.last(); i= Integer.parseInt(rs.getString(1))+1;
			 }
			rs.close();
			
			
			rs=st.executeQuery("select farmer_id from farmer where user_name='"+username+"'");
			rs1=st1.executeQuery("select c_id from crop where c_name='"+crop[0]+"'"); 

			if(rs.next()==true&&rs1.next()==true)
			{ 
					String fid=rs.getString(1); String cid=rs1.getString(1);
						
					if(st2.executeUpdate("insert into farmer_crop values("+i+","+rs.getString(1)+",'"+rs1.getString(1)+"','"+date+"','0000-00-00',NULL,NULL,NULL,NULL)")>=1)
					{
						System.out.println("inserted successfully");
					}
					
					 rs.close(); rs1.close();
					 
					 rs2=st3.executeQuery("select c_id from crop where c_name='"+crop[0]+"'");
					 
					 if(rs2.next())rs=st1.executeQuery("select e_name,e_id from event where e_id in (select e_id from crop_event where event.e_id=crop_event.e_id and '"+rs2.getString(1)+"'=crop_event.c_id)");
                     rs1=st2.executeQuery("select e_id,no_of_days from crop_event where crop_event.c_id='"+rs2.getString(1)+"'");
			         int nod=0;
					while(rs.next()&& rs1.next()){
					   st.executeUpdate("insert into farmer_crop_update values("+i+",'"+rs.getString(2)+"','"+LocalDate.now().plusDays(nod)+"',NULL,NULL)");
					   nod+=rs1.getInt(2);
					}
					rs.close();
				    rs1.close();
					response.getWriter().println("<script type='text/javascript'>window.alert('Crop "+crop[0]+"  Added Successfully !!!');</script>");
					rd=request.getRequestDispatcher("Farmer_Login_success.jsp");
					rd.include(request, response);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
