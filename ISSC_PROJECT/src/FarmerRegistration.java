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


@WebServlet("/FarmerRegistration")
public class FarmerRegistration extends HttpServlet {

	String firstname,question,answer,lastname,faddress,mobileno,email,username,password,confirmPassword;
	String  adhar;
	RequestDispatcher rd;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		firstname=request.getParameter("firstname");
		lastname=request.getParameter("lastname");
		faddress=request.getParameter("paddress");
		mobileno=request.getParameter("mobileno");
		email=request.getParameter("email");
		adhar=request.getParameter("adhar");
		username=request.getParameter("username");
		password=request.getParameter("password");
		confirmPassword=request.getParameter("confirmPassword");
		question =request.getParameter("question");
		answer=request.getParameter("answer");
		
		
		System.out.println(firstname+" "+lastname+" "+faddress+" "+mobileno+" ");
		try {
			this.insert(request,response);
		} 
		catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	public void insert(HttpServletRequest request,HttpServletResponse response) throws ClassNotFoundException, IOException, ServletException
	{
		PrintWriter out;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/croptracking","root","root");
			System.out.println("Connection successfull");
			PreparedStatement ps=conn.prepareStatement("insert into farmer values(?,?,?,?,?,?,?,?,?,?,?)");

			Statement st=conn.createStatement();
			ResultSet rs= st.executeQuery("select * from farmer");

			if(rs.next()==false)
			{ System.out.println("No data in farmer");
			  ps.setInt(1, 1);
			}
			else{
				rs.last();
				ps.setInt(1, rs.getInt(1)+1);

				rs=st.executeQuery("select user_name password from farmer");
				while(rs.next())
				{
					if(username.equals(rs.getString(1))==true)
					{
						out=response.getWriter();

						out.println("<font color='red'><b> username "+username+" already exists, Enter Different username</b></font>");
						 rd = request.getRequestDispatcher("Register.html");
						 rd.include(request, response); return;
						//System.exit(0); 
					}
				}

			}
			ps.setString(2, firstname); 
			ps.setString(3, lastname);
			ps.setString(4,faddress ); 
			ps.setString(5, mobileno);
			ps.setString(6, email);
			ps.setString(7, adhar);  
			ps.setString(8, username); 
			ps.setString(9, password);  
			ps.setString(10, question);
            ps.setString(11, answer);
			
			if(ps.executeUpdate()>=1)
			{
				System.out.println("\nInserted Record Successfully");
				response.getWriter().println("<script>window.alert('!!! Registered Successfully !!!');</script>");
				rd = request.getRequestDispatcher("homePage.html");
				rd.include(request, response);
			}
		}
		catch(SQLException e)
		{   e.printStackTrace();
		    System.out.println("sql exception");
		}
	}
}


