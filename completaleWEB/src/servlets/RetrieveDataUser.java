package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import classes.JSONParser;
import daoImpl.DAOManager;
import entities.User;

/**
 * Servlet implementation class FillChangeDataForm
 */
@WebServlet("/RetrieveDataUser")
public class RetrieveDataUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
	
    public RetrieveDataUser() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		daoManager = new DAOManager (em,ut);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Si tiene sesión
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		if(username!=null){
			
			// Buscamos al usuario
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveDataUser");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Creamos la respuesta en JSON
			String [] array = {"username", user.getNick(), "email", user.getEmail()}; 
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(JSONParser.parserJSONObjects(array));			
			out.flush();
			out.close();
		}
		// Si accede por /RetrieveDataUser
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}
