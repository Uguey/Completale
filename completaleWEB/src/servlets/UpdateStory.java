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
import javax.transaction.UserTransaction;

import classes.JSONParser;
import daoImpl.DAOManager;
import entities.Fragment;
import entities.Story;
import entities.User;

@WebServlet("/UpdateStory")
public class UpdateStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public UpdateStory() {
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
				
			// Obtenemos el título de la historia
			String title = (String) request.getParameter("title");
			
			// Obtenemos la historia correspondiente a ese usuario y ese título, que esté abierta
			List <Story> storiesIParticipate = daoManager.getStoriesFromParticipant(user);
			Iterator <Story> storiesIParticipateIterator = storiesIParticipate.iterator();
			Story storyIAmParticipating = null;
			int i=0;
			while (storiesIParticipateIterator.hasNext()){
				Story currentStory = storiesIParticipateIterator.next();
				if (currentStory.getTitle().equals(title)){
					storyIAmParticipating = currentStory;
					i++;
				}
			}
			if (i>1) System.out.println("Hay más de una historia con el mismo título");
			
			// Vemos si la historia está abierta
			String openedStory = "";
			byte state = storyIAmParticipating.getState();
			if (state==0) openedStory="false";
			else openedStory="true";
			
			// Comparamos el usuario con el participante actual
			String actualParticipant="";
			if (storyIAmParticipating.getActualParticipant().equals(user.getNick())) actualParticipant="true";
			else actualParticipant="false";
				
			// Construimos la respuesta diciendo si el participante actual es el usuario y con los fragmentos
			String [] array = new String [2+storyIAmParticipating.getFragments().size()];
			array[0]=openedStory;
			array[1]=actualParticipant;
			for (i=2;i<(storyIAmParticipating.getFragments().size()+2);i++){
				List <Fragment> fragmentsStoryIAmParticipating =storyIAmParticipating.getFragments();
				array [i] = fragmentsStoryIAmParticipating.get(i-2).getFragment();
			}	
				
			String responseJSON = JSONParser.parserJSONArray(array);
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(responseJSON);			
			out.flush();
			out.close();
			}
		}
		
		// Si accede por /AddNewFragment
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
}

















