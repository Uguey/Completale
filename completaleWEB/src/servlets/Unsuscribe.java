package servlets;

import java.io.IOException;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import daoImpl.DAOManager;
import entities.User;

/**
 * Servlet implementation class Unsuscribe
 */
@WebServlet("/Unsuscribe")
public class Unsuscribe extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public Unsuscribe() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException{
    	super.init(config);
    	daoManager = new DAOManager (em,ut);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		// Si tiene sesión
		if (username!=null){
			
			// Invalidamos la sesión
			session.invalidate();
			
			// Encontramos al usuario
			List<User>users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en Unsuscribe");
			Iterator<User>usersIterator=users.iterator();
			User user = null;
			while(usersIterator.hasNext()){
				user=usersIterator.next();
			}
			if(user==null)System.out.println("Error: El usuario no existe en Unsuscribe");

			// Eliminamos el usuario
			try {
				daoManager.removeUser(user);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}

			// Vamos a initio
			this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
		}
		
		// Si accede por /Unsuscribe
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}

}
