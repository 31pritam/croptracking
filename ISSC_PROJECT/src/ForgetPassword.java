

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

/**
 * Servlet implementation class ForgetPassword
 */
@WebServlet("/ForgetPassword")
public class ForgetPassword extends HttpServlet {
	Connection conn;
	Statement st;
	String question,answer,username;
	RequestDispatcher rd;
	static final String ALPHA_NUMERIC_STRING = "AGHIJKLMN0123OPQRSTUVWXYZ4567DEF8BC9";
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		question =request.getParameter("question");
		 answer=request.getParameter("answer");
		 username=request.getParameter("username");

		  try {
			checkQuestion(request,response);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
	
	
   void checkQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException
   {
	   Class.forName("com.mysql.jdbc.Driver");
	   conn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking", "root", "root");
	   st=(Statement) conn.createStatement();
	   
	   ResultSet rs=st.executeQuery("select * from farmer where user_name='"+username+"' and question='"+question+"' and answer='"+answer+"'");
		 
	   if(rs.next()){
		    System.out.println("details found");
		    
		    StringBuilder builder = new StringBuilder();
		    int count=8;
			while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
			}
		    
			String pass=builder.toString();
		    response.getWriter().println("<script>alert('Your Password Is:"+pass+"');</script>");
		    rs.close();
		    
		    int i=st.executeUpdate("update farmer set password='"+pass+"' where user_name='"+username+"'");
		    
		    rd=request.getRequestDispatcher("forget.html");
		    rd.include(request, response);
		 }
	 else
	 {     System.out.println("details not found");
		    
	        response.getWriter().println("<script>alert('You have entered Incorrect Details');</script>");
		    rd=request.getRequestDispatcher("forget.html");
		    rd.include(request, response);
	 }
	 st.close();
	 conn.close();
   }
}
