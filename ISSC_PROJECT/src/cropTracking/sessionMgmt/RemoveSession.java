package cropTracking.sessionMgmt;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RemoveSession")
public class RemoveSession extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		removeSession(request,response);
        response.sendRedirect("temp.jsp");
	}
    
	public static void removeSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session=request.getSession();
		
        session.removeAttribute("username");
        session.removeAttribute("farmerID");
        session.invalidate();
	}
}