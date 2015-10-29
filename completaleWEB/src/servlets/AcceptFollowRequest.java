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

@WebServlet("/AcceptFollowRequest")
public class AcceptFollowRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public AcceptFollowRequest() {
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
			
			// Obtenemos el usuario
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos al seguidor
			String nickFollower = (String) request.getParameter("nickFollower");
			List<User> followersList = daoManager.getUser(nickFollower);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			User follower=null;
			Iterator<User>followersListIterator=followersList.iterator();
			while(followersListIterator.hasNext()==true){
				follower=followersListIterator.next();
			}
			
			// Quitamos al seguidor de las peticiones de seguimiento recibidas
			List <User> followingRequests = user.getFollowingRequests();
			Iterator <User> followingRequestsIterator = followingRequests.iterator();
			int i=0;
			int positionRemove=-1;
			while (followingRequestsIterator.hasNext()){
				User currentFollower = followingRequestsIterator.next();
				if ((currentFollower.getNick().equals(follower.getNick()))&&(currentFollower.getEmail().equals(follower.getEmail()))){
					positionRemove=i;
				}
				i++;
			}
			if (positionRemove!=-1)	followingRequests.remove(positionRemove);
			
			// Agregamos al seguidor a la lista de followers
			List <User> followers = user.getFollowings();
			followers.add(follower);
			try {
				daoManager.mergeUser(user);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Si accede por /AcceptFollowRequest
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}

}
