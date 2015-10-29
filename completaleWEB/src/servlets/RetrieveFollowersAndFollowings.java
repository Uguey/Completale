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

@WebServlet("/RetrieveFollowersAndFollowings")
public class RetrieveFollowersAndFollowings extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
       
    public RetrieveFollowersAndFollowings() {
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
			
			// Obtenemos los followers y followings
			List <User> followers = user.getFollowings();
			List <String> followersName = new ArrayList <String>();
			Iterator <User> followersNameIterator = followers.iterator();
			while (followersNameIterator.hasNext()){
				followersName.add(followersNameIterator.next().getNick());
			}
			
			// Obtenemos los followings a partir de sus nombres
			List <String> followingsName = daoManager.getFollowings(user.getNick());
			List <User> followings = new ArrayList <User>();

			Iterator<String>followingsNameIterator=followingsName.iterator();
			while(followingsNameIterator.hasNext()==true){
				String currentFollowingName = followingsNameIterator.next();
				
				List <User> currentFollowing = daoManager.getUser(currentFollowingName);
				if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
				User currentUser=null;
				Iterator<User>currentFollowingIterator=currentFollowing.iterator();
				while(currentFollowingIterator.hasNext()==true){
					currentUser=currentFollowingIterator.next();
				}
				
				followings.add(currentUser);				
			}

			// Obtenemos los likes de cada follower y following
			Map <User, Integer> followerLikes = LikesAndIfIFollowAllFollower.getLikes(followers);
			List <Integer> followerLikesList = new ArrayList <Integer> ();
			Iterator <User> followersIterator = followers.iterator();
			while(followersIterator.hasNext()){
				User currentUser = followersIterator.next();
				followerLikesList.add(followerLikes.get(currentUser));
			}			
			
			Map <User, Integer> followingLikes = LikesAndIfIFollowAllFollower.getLikes(followings);
			List <Integer> followingLikesList = new ArrayList <Integer> ();
			Iterator <User> followingIterator = followings.iterator();
			while(followingIterator.hasNext()){
				User currentUser = followingIterator.next();
				followingLikesList.add(followingLikes.get(currentUser));
			}	
			
			// Obtenemos por cada follower si le seguimos o no
			Map <User, Boolean> ifIFollowFollower = LikesAndIfIFollowAllFollower.getIfIFollowFollower(followers,followingsName);
			List <Boolean> ifIFollowFollowerList = new ArrayList <Boolean> ();
			followersIterator = followers.iterator();
			while(followersIterator.hasNext()){
				User currentUser = followersIterator.next();
				ifIFollowFollowerList.add(ifIFollowFollower.get(currentUser));
			}	
			
			// Rellenamos la respuesta
			request.setAttribute("FollowersOrFollowings", (String) request.getParameter("active"));
			
			request.setAttribute("Followers", followersName);
			request.setAttribute("followerLikes", followerLikesList);
			request.setAttribute("ifIFollowFollower", ifIFollowFollowerList);
			
			request.setAttribute("Followings", followingsName);
			request.setAttribute("followingLikes", followingLikesList);
			
			// Vamos a FollowUp
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/FollowUp.jsp").forward(request, response);			
		}
		
		// Si accede por /CloseSession
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}









