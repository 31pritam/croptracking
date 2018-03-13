package cropTracking.sessionMgmt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/RemoveAdminSession")
public class RemoveAdminSession extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		removeAdminSession(request,response);
        response.sendRedirect("temp.jsp");
	}
    
	public static void removeAdminSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session=request.getSession();
		
        session.removeAttribute("admin");
        session.invalidate();
        
        System.out.println("!!!! Inside Admin Session Remove Class !!!!!");
	}
}
