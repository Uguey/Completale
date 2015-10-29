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
import javax.transaction.UserTransaction;

import daoImpl.DAOManager;
import entities.Fragment;
import entities.Story;
import entities.StoryPK;
import entities.User;

@WebServlet("/SeeStory")
public class SeeStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public SeeStory() {
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
			
			String title = (String) request.getParameter("title");
			String access = (String) request.getParameter("access");
			
			// Obtenemos la historia que tenga este título
			List <Story> stories= daoManager.getStoriesFromTitle(title);
			if (stories.size()>1) System.out.println("Hay más de una historia con el mismo título");
			Story story= stories.get(0);
			
			// Ordenamos los fragmentos
			List <Fragment> fragments = story.getFragments();			
			if (fragments!=null) {
				
				for (int i=0; i<fragments.size();i++){
					for (int j=0; j<fragments.size()-1;j+=2){
						Fragment currentFragment = fragments.get(j);
						Fragment nextFragment = fragments.get(j+1);
						int currentID = currentFragment.getId().getId();
						int nextID = nextFragment.getId().getId();
						if (currentID>nextID){
							fragments.set(j, nextFragment);
							fragments.set(j+1, currentFragment);
						}
						else if (currentID==nextID) System.out.println("Problema asignando los id en SeeStory");
					}
				}
				 
				// Colocamos los fragmentos en un String
				String fragment = "";
				for (int i=0;i<fragments.size();i++){
					if (i==0) fragment = fragments.get(i).getFragment();
					else {
						fragment += " ";
						fragment += fragments.get(i).getFragment();
					}
				}
				request.setAttribute("story", fragment);
			}
			
			// Obtenemos el usuario
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Construimos la respuesta
			request.setAttribute("title", story.getTitle());
					
			if (access.equals("leader"))
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/SeeStoryNoLike.jsp").forward(request, response);
			
			else if (access.equals("participant")){
				
				// Obtenemos su historia
				List<Story> myActiveStories = daoManager.getActiveStoriesFromParticipant (user);
				if (myActiveStories.size()>1) System.out.println("Este usuario está participando en más de una historia a la vez en SeeStory");
				Story myActiveStory = myActiveStories.get(0);
				StoryPK myActiveStoryPK = myActiveStory.getId();
				StoryPK storyPK = story.getId();				
				
				// Si la historia es la misma que la que ha seleccionado, vamos a No Like
				if ((myActiveStoryPK.getLeader().equals(storyPK.getLeader()))
					&&(myActiveStoryPK.getOpenDate()==storyPK.getOpenDate())){
					this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/SeeStoryNoLike.jsp").forward(request, response);
				}
				
				// Si la historia no es la que él ha creado, vamos a Like
				else {
					this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/SeeStory.jsp").forward(request, response);
				}
			}
			else if (access.equals("timeline")){
				
				StoryPK storyPK = story.getId();
				
				boolean iPressedOnMyStory = false;
				
				// Obtenemos todas sus historias
				List<Story> myStories = daoManager.getStoriesFromParticipant (user);
				Iterator <Story> myStoriesIterator = myStories.iterator();
				while (myStoriesIterator.hasNext()){
					Story currentMyStory = myStoriesIterator.next();
					StoryPK currentMyStoryPK = currentMyStory.getId();
					
					// Si una de sus historias es la misma que la que ha seleccionado
					if ((currentMyStoryPK.getLeader().equals(storyPK.getLeader()))
							&&(currentMyStoryPK.getOpenDate()==storyPK.getOpenDate())){
						iPressedOnMyStory= true;
					}
				}
				
				// Si la historia es la misma que la que ha seleccionado, vamos a No Like				
				if (iPressedOnMyStory) 
					this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/SeeStoryNoLike.jsp").forward(request, response);
				
				// Si no, vamos a like
				else 
					this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/SeeStory.jsp").forward(request, response);
				
			}
			else System.out.println("No se coge bien el atributo access");
		}
	
		// Si accede por /SeeStory
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}
