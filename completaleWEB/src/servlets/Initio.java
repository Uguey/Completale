package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InitioServlet
 */
@WebServlet("")
public class Initio extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	private String failChangeData=null;
	       
    public Initio() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		if (username==null){
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/SignUpLogIn.jsp").forward(request, response);
		}
		else {
			if (request.getAttribute("failChangeData")!=null) failChangeData=(String) request.getAttribute("failChangeData");	
			if (failChangeData!=null)request.setAttribute("failChangeData", failChangeData);
			
			this.getServletConfig().getServletContext().getRequestDispatcher("/DataCharger").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		if (request.getAttribute("failChangeData")!=null) failChangeData=(String) request.getAttribute("failChangeData");	
		doGet(request,response);
	}
}
