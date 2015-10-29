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
import entities.Story;
import entities.User;

@WebServlet("/FinishStory")
public class FinishStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public FinishStory() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		daoManager = new DAOManager (em,ut);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		// Si  hay sesión
		if(username!=null){
			
			// Obtenemos el usuario
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos la historia activa en la que el usuario está participando
			List <Story> activeStories = daoManager.getActiveStoriesFromParticipant (user);
			if (activeStories.size()>1) System.out.println("Error: El usuario está participando en más de una historia en en DataCharger");
			Story activeStory = activeStories.get(0);
			
			// Cambiamos su estado a cerrada
			activeStory.setState((byte) 0);
			
			// Mergeamos
			try {
				daoManager.mergeStory(activeStory);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Vamos a DataCharger
			this.getServletConfig().getServletContext().getRequestDispatcher("/DataCharger").forward(request, response);
		}
		
		// En cualquier otro caso
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);		
	}
}
