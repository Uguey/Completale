package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CloseSession
 */
@WebServlet("/CloseSession")
public class CloseSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CloseSession() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// Si tiene sesión
		if (session.getAttribute("username")!=null){
			session.invalidate();
			this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
		}
		
		// Si accede por /CloseSession
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}
