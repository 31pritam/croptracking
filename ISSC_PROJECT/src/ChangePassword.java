

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


/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	String username,cp,np; 
	Connection con;
	Statement st;
    RequestDispatcher rd;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   cp=request.getParameter("currentpass");
	   np=request.getParameter("newpass");
	   username=request.getParameter("username");
	   
	   try {
		changePass(request,response);
	} catch (ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public void changePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException 
	{
		   Class.forName("com.mysql.jdbc.Driver");
		   con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking", "root", "root");
		   st=(Statement) con.createStatement();
		   int i=st.executeUpdate("update farmer set password='"+np+"' where user_name='"+username+"' and password='"+cp+"'");
		   
		   if(i>=1)
		   {
			    response.getWriter().println("<script type='text/javascript'>window.alert('Password Changed Successfully!!!');</script>");
				rd=request.getRequestDispatcher("changePassword.html");
				rd.include(request, response);
		   }
		   else
		   {
			   response.getWriter().println("<script type='text/javascript'>window.alert('Password Is Not Changed !!!');</script>");
				rd=request.getRequestDispatcher("changePassword.html");
				rd.include(request, response);
		   }
		   
	}
	
}
