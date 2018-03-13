package cropTracking.sessionMgmt;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CropSession")
public class CropSession extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
    
	public static boolean isSet(HttpServletRequest request, HttpServletResponse response,int type) throws ServletException, IOException {
		 
		 String name=null;
		
         HttpSession session=request.getSession();
		 if(type==1) name=(String) session.getAttribute("username");
		 else if(type==2) name=(String) session.getAttribute("admin");
		 
		if(name!=null)
		return true;
		else return false;
	}
}

