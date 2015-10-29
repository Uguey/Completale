package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import classes.LikesAndIfIFollowAllFollower;
import daoImpl.DAOManager;
import entities.User;

@WebServlet("/RetrieveFollowingRequests")
public class RetrieveFollowingRequests extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public RetrieveFollowingRequests() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		daoManager = new DAOManager (em,ut);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			
			// Obtenemos los usuarios que han enviado una solicitud de seguimiento
			List <User> possibleFollowers = user.getFollowingRequests();
			List <String> possibleFollowersName = new ArrayList <String>();
			Iterator <User> possibleFollowersIterator = possibleFollowers.iterator();
			while (possibleFollowersIterator.hasNext()){
				possibleFollowersName.add(possibleFollowersIterator.next().getNick());
			}
			
			// Obtenemos los likes de cada usuario que ha enviado una solicitud de seguimiento
			Map <User, Integer> possibleFollowersLikesMap = LikesAndIfIFollowAllFollower.getLikes(possibleFollowers);
			List <Integer> possibleFollowersLikesList = new ArrayList <Integer> ();
			possibleFollowersIterator = possibleFollowers.iterator();
			while(possibleFollowersIterator.hasNext()){
				User currentUser = possibleFollowersIterator.next();
				possibleFollowersLikesList.add(possibleFollowersLikesMap.get(currentUser));
			}
			
			// Rellenamos la respuesta
			request.setAttribute("possibleFollowersName", possibleFollowersName);
			request.setAttribute("possibleFollowersLikes", possibleFollowersLikesList);
			
			// Vamos a FollowingRequests
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/FollowingRequests.jsp").forward(request, response);			
		}
		
		// Si accede por /CloseSession
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}
