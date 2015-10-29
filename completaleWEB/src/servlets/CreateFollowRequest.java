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

@WebServlet("/CreateFollowRequest")
public class CreateFollowRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
       
    public CreateFollowRequest() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		daoManager = new DAOManager (em,ut);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		// Si tiene sesión
		if (username!=null){
			
			// Obtenemos a quien queremos seguir
			String nickPossibleFollow = (String) request.getParameter("nickPossibleFollow");
			
			// Obtenemos el usuario como entidad User
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en CreateFollowRequest");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos a quien queremos seguir como entidad User
			List<User> possibleFollowUsers = daoManager.getUser(nickPossibleFollow);
			if(possibleFollowUsers.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en CreateFollowRequest");
			User possibleFollow=null;
			Iterator<User>possibleFollowUsersIterator=possibleFollowUsers.iterator();
			while(possibleFollowUsersIterator.hasNext()==true){
				possibleFollow=possibleFollowUsersIterator.next();
			}
			
			// Nos añadimos a nosotros como pedidores de seguimiento del otro usuario
			List <User> followingRequests = possibleFollow.getFollowingRequests();
			followingRequests.add(user);
			possibleFollow.setFollowersRequests(followingRequests);
			
			// Mergeamos en la base de datos a ambos usuarios
			try {
				daoManager.mergeUser(possibleFollow);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		// Si accede por /CreateFollowRequest
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}
