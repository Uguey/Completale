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

import classes.FillerForMainMainStoriesAndChallenges;
import classes.StoriesFromStoryPKRaw;
import daoImpl.DAOManager;
import entities.Challenge;
import entities.Fragment;
import entities.Story;
import entities.StoryPK;
import entities.User;
import entities.Wrap;

@WebServlet("/AcceptParticipationStory")
public class AcceptParticipationStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
       
    public AcceptParticipationStory() {
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
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveStoriesParticipationRequests");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
		    // Obtenemos el título de la historia que vamos a aceptar
			String title = (String) request.getParameter("Title");
			
			// Obtenemos las historias que han enviado una solicitud de seguimiento
			List<?> storiesRequestsPKRaw = daoManager.getReceivedParticipationRequests(user.getNick());	
			List <StoryPK> storiesRequestsPK = StoriesFromStoryPKRaw.getStoriesPKFromStoriesPKRaw(storiesRequestsPKRaw);
			List <Story> storiesRequests = StoriesFromStoryPKRaw.getStoriesFromStoriesPK(storiesRequestsPK, daoManager);	
			
			// Obtenemos la historia que se corresponde con nuestro título de historia
			Iterator <Story> storiesRequestsIterator = storiesRequests.iterator();
			Story storyAboutToParticipate = null;
			while(storiesRequestsIterator.hasNext()){
				Story currentStory = storiesRequestsIterator.next();
				if (currentStory.getTitle().equals(title)){
					storyAboutToParticipate = currentStory;
				}
			}
			
			if (storyAboutToParticipate.getState()==0)
				System.out.println("Error: La historia está cerrada cuando debería estar abierta en AcceptParticipationStory");
			
			// Sacamos la solicitud de participación de la historia y metemos al usuario como participante de la historia
			List <User> usersParticipating = storyAboutToParticipate.getUsersParticipating();
			usersParticipating.add(user);
			storyAboutToParticipate.setUsersParticipating(usersParticipating);
			List <User> usersParticipationRequests = storyAboutToParticipate.getUsersSentParticipationRequests();
			Iterator <User> usersParticipationRequestsIterator = usersParticipationRequests.iterator();
			int i=0;
			int positionToRemove=-1;
			while(usersParticipationRequestsIterator.hasNext()){
				User currentUserParticipationRequests = usersParticipationRequestsIterator.next();
				if ((currentUserParticipationRequests.getNick().equals(user.getNick()))
					&&(currentUserParticipationRequests.getEmail().equals(user.getEmail()))){
					positionToRemove=i;
				}
				i++;
			}
			if (positionToRemove!=-1) usersParticipationRequests.remove(positionToRemove);
			storyAboutToParticipate.setUsersSentParticipationRequests(usersParticipationRequests);	
			
			// Mergeamos la historia
			try {
				daoManager.mergeStory(storyAboutToParticipate);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Vemos si la historia procede de un reto
			Wrap ifStoryIsInChallengeWrap = storyAboutToParticipate.getChallenge();
						
			// Si no es de un reto
			if (ifStoryIsInChallengeWrap==null){			
			
				// Recuperamos los datos de la historia 
				title = storyAboutToParticipate.getTitle();
				List <Fragment> fragments = daoManager.getFragmentsOrdered(storyAboutToParticipate);
				List <String> fragmentsString = new ArrayList <String>();
				Iterator <Fragment> fragmentsIterator = fragments.iterator();
				while (fragmentsIterator.hasNext()){
					fragmentsString.add(fragmentsIterator.next().getFragment());
				}
				
				// Colocamos los datos del usuario para mainStories
				request = FillerForMainMainStoriesAndChallenges.retrieveAndSetData(request, user.getNick(), daoManager);
									
				// Creamos la respuesta
				request.setAttribute ("Title", title);
				request.setAttribute ("Fragments", fragmentsString);
				
				// Reenviamos a mainStories						
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainStories.jsp").forward(request, response);
			}
			
			// Si es de un reto
			else {
				
				Challenge ifStoryIsInChallenge = ifStoryIsInChallengeWrap.getChallenge();
				
				// Colocamos los datos del usuario para mainStories
				request = FillerForMainMainStoriesAndChallenges.fillMainAndMainChallengesParticipant
						(request, user.getNick(), daoManager, ifStoryIsInChallenge, storyAboutToParticipate);
				
				// Reenviamos a mainChallengesParticipant			
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainChallengesParticipant.jsp").forward(request, response);
			}
		}
		
		// Si accede por /AcceptParticipationStory
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);	
	}
}
