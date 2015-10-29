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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import classes.JSONParser;
import daoImpl.DAOManager;
import entities.Fragment;
import entities.FragmentPK;
import entities.Story;
import entities.StoryPK;
import entities.User;

@WebServlet("/AddNewFragment")
public class AddNewFragment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public AddNewFragment() {
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
			
			// Obtenemos el título y el fragmento a añadir
			String title = (String) request.getParameter("title");
			String fragment = (String) request.getParameter("fragment");
			
			// Obtenemos la historia correspondiente a ese usuario y ese título, que esté abierta
			List <Story> storiesIParticipate = daoManager.getActiveStoriesFromParticipant(user);
			if(storiesIParticipate.size()>1) System.out.println("Hay más de una historia abierta");
			Iterator <Story> storiesIParticipateIterator = storiesIParticipate.iterator();
			Story storyIAmParticipating = null;
			while (storiesIParticipateIterator.hasNext()){
				Story currentStory = storiesIParticipateIterator.next();
				if (currentStory.getTitle().equals(title)){
					storyIAmParticipating = currentStory;
				}
			}
			
			// Añadimos un fragmento más a la historia
			Fragment fragmentToAdd = this.createFragment(fragment, storyIAmParticipating, user);
			List <Fragment> fragments = storyIAmParticipating.getFragments();
			fragments.add(fragmentToAdd);
			storyIAmParticipating.setFragments(fragments);		
			
			// Actualizamos el participante actual
			storyIAmParticipating = this.updateParticipant(storyIAmParticipating);
			
			// Mergeamos la historia
			try {
				daoManager.mergeStory(storyIAmParticipating);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Comparamos el usuario con el participante actual
			String actualParticipant="";
			if (storyIAmParticipating.getActualParticipant().equals(user.getNick())) actualParticipant="true";
			else actualParticipant="false";
			
			// Construimos la respuesta diciendo si el participante actual es el usuario y con los fragmentos
			String [] array = new String [1+storyIAmParticipating.getFragments().size()];
			array[0]=actualParticipant;
			for (int i=1;i<(storyIAmParticipating.getFragments().size()+1);i++){
				List <Fragment> fragmentsStoryIAmParticipating =storyIAmParticipating.getFragments();
				array [i] = fragmentsStoryIAmParticipating.get(i-1).getFragment();
			}
			
			
			String responseJSON = JSONParser.parserJSONArray(array);
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(responseJSON);			
			out.flush();
			out.close();
			
		}
		
		// Si accede por /AddNewFragment
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
	
	private Fragment createFragment (String fragmentString, Story story, User writer){
		Fragment fragment = null;
		FragmentPK fragmentPK = null;
		StoryPK storyPK = story.getId();
		
		List <Fragment> fragments = daoManager.getFragmentsOrdered(story);
		Iterator <Fragment> fragmentsIterator = fragments.iterator();
		Fragment LastFragment=null;
		while (fragmentsIterator.hasNext()){
			LastFragment=fragmentsIterator.next();
		}
		
		int id = 1;
		if (LastFragment==null) id=1;
		else id = LastFragment.getId().getId() + 1;
		
		fragmentPK = new FragmentPK (storyPK.getLeader(), storyPK.getOpenDate(), id);
		
		fragment = new Fragment(fragmentPK, story, writer, fragmentString);
		
		
		return fragment;
	}
	
	private Story updateParticipant (Story storyIAmParticipating){
		
		List <User> participants = storyIAmParticipating.getUsersParticipating();
		
		String currentParticipantString = storyIAmParticipating.getActualParticipant();
		List<User> currentParticipants = daoManager.getUser(currentParticipantString);
		if(currentParticipants.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
		User currentParticipant=null;
		Iterator<User>currentParticipantsIterator=currentParticipants.iterator();
		while(currentParticipantsIterator.hasNext()==true){
			currentParticipant=currentParticipantsIterator.next();
		}
		
		Iterator <User> participantsIterator = participants.iterator();
		User nextParticipant=null;
		while(participantsIterator.hasNext()){				
			User participantInList = participantsIterator.next();			
			if ((participantInList.getNick().equals(currentParticipant.getNick()))
				&&(participantInList.getEmail().equals(currentParticipant.getEmail())))
				break;			
		}
	
		if (participantsIterator.hasNext()) nextParticipant=participantsIterator.next();
		else nextParticipant = participants.get(0);
		
		storyIAmParticipating.setActualParticipant(nextParticipant.getNick());
		
		return storyIAmParticipating;
	}
}






















