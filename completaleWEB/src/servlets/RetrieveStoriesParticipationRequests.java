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
import javax.transaction.UserTransaction;

import classes.StoriesFromStoryPKRaw;
import daoImpl.DAOManager;
import entities.Story;
import entities.StoryPK;
import entities.User;

@WebServlet("/RetrieveStoriesParticipationRequests")
public class RetrieveStoriesParticipationRequests extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
       
    public RetrieveStoriesParticipationRequests() {
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
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveStoriesParticipationRequests");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos las historias que han enviado una solicitud de seguimiento al usuario
			List<?> storiesRequestsPKRaw = daoManager.getReceivedParticipationRequests(user.getNick());	
			List <StoryPK> storiesRequestsPK = StoriesFromStoryPKRaw.getStoriesPKFromStoriesPKRaw(storiesRequestsPKRaw);
			List <Story> storiesRequests = StoriesFromStoryPKRaw.getStoriesFromStoriesPK(storiesRequestsPK, daoManager);
			storiesRequests = retrieveOnlyOpenedStories(storiesRequests);
			
			// Obtenemos los títulos de las historias que han enviado una solicitud de seguimiento
			List <String> storiesRequestsTitles = new ArrayList <String>();
			Iterator <Story> storiesRequestsIterator = storiesRequests.iterator();
			while (storiesRequestsIterator.hasNext()){
				Story currentStory = storiesRequestsIterator.next();
				storiesRequestsTitles.add(currentStory.getTitle());
			}			
			
			// Obtenemos los likes de las historias que han enviado una solicitud de seguimiento
			List <Integer> likes = new ArrayList <Integer>();
			storiesRequestsIterator = storiesRequests.iterator();
			while (storiesRequestsIterator.hasNext()){
				likes.add(storiesRequestsIterator.next().getLikes());
			}

			// Rellenamos la respuesta
			request.setAttribute("Titles", storiesRequestsTitles);
			request.setAttribute("Likes", likes);
			
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/StoryParticipationRequests.jsp").forward(request, response);			
		}
		
		// Si accede por /CloseSession
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);

	}
	
	private List <Story> retrieveOnlyOpenedStories (List <Story> storiesRequests){
		
		Iterator <Story> storiesRequestsIterator = storiesRequests.iterator();
		ArrayList <Integer> removeStories = new ArrayList <Integer>();
		int i=0;
		while (storiesRequestsIterator.hasNext()){
			Story currentStory = storiesRequestsIterator.next();
			if (currentStory.getState()==0){
				removeStories.add(i);
			}
			i++;
		}
		Iterator <Integer> removeStoriesIterator = removeStories.iterator();
		while (removeStoriesIterator.hasNext()){
			storiesRequests.remove(removeStoriesIterator.next());
		}
		return storiesRequests;
	}
}
