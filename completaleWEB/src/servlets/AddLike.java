package servlets;

import java.io.IOException;
import java.util.ArrayList;
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

@WebServlet("/AddLike")
public class AddLike extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public AddLike() {
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
			
			String title = (String) request.getParameter("title");
			
			// Obtenemos la historia que tenga este título
			List <Story> stories= daoManager.getStoriesFromTitle(title);
			if (stories.size()>1) System.out.println("Hay más de una historia con el mismo título");
			Story story= stories.get(0);
			
			// Añadimos un Like
			int likes = story.getLikes();
			likes++;
			story.setLikes(likes);
			
			// Guardamos en la base de datos
			try {
				daoManager.mergeStory(story);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		// Si accede por /AddLike
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);	
	}
	
	public List <Story> retrieveStories (List <User> followings){
		
		List <Story> stories = new ArrayList <Story>();
		Iterator <User> followingsIterator = followings.iterator();
		while (followingsIterator.hasNext()){
			User currentFollowing = followingsIterator.next();
			List <Story> storiesCurrentFollowing = daoManager.getStoriesFromParticipant(currentFollowing);
			Iterator <Story> storiesCurrentFollowingIterator = storiesCurrentFollowing.iterator();
			while (storiesCurrentFollowingIterator.hasNext()){
				stories.add(storiesCurrentFollowingIterator.next());
			}
		}		
		return stories;
	}
}





