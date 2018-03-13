

import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ValidateLogin
 */
@WebServlet("/ValidateLogin")
public class ValidateLogin extends HttpServlet {
	
	String username,password,who;
    Statement st;
    Connection conn;
    ResultSet rs;
    PrintWriter out;
    RequestDispatcher rd;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 out=response.getWriter();
		 //out.println("<h1>Welcome"+request.getParameter("username"));
		
		username=request.getParameter("username");
		password=request.getParameter("password");
		who=request.getParameter("type");
		
		System.out.println("!!!! Validate login !!!\n username:"+username+" password: "+password);
		
		if(who.equals("farmer")==true)
		{
			try {
				isFarmer(request,response);
			   } catch(Exception e){ }
		}
		if(who.equals("admin")==true)
		{       
			if(username.equals("admin")&&password.equals("password")){
				HttpSession session = request.getSession();
			    session.setMaxInactiveInterval(20*60);
	            session.setAttribute("admin","admin");
				response.sendRedirect("Admin_Login_Success.html");
			}
			else{
				out.println("<font color='red'><b>You have entered incorrect Username or password</b></font>");
				rd=request.getRequestDispatcher("homePage.html");
				rd.include(request,response);
			}		
		}
		
	}
	
	public void isFarmer(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException, ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
		st=conn.createStatement();
		
		try{ 										//SELECT *  FROM crop WHERE BINARY c_name = 'TOMATo';
			rs=st.executeQuery("select farmer_id,farmer_firstname, user_name, password from farmer where BINARY user_name='"+username+"' and BINARY password='"+password+"'");
			
			if(rs.next()==true)
			{
				   
				    HttpSession session = request.getSession();
				    session.setMaxInactiveInterval(20*60);
		            session.setAttribute("farmerID", rs.getString(1));
		            session.setAttribute("username", rs.getString(3));
				    response.sendRedirect("Farmer_Login_success.jsp");
				    
			}
			else
			{
				out.println("<font color='red'><b>You have entered incorrect Username or password</b></font>");
				rd=request.getRequestDispatcher("homePage.html");
				rd.include(request,response);
			}		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}














