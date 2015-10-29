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
import entities.Challenge;
import entities.Story;
import entities.User;
import entities.Wrap;

@WebServlet("/UpdateChallengeParticipant")
public class UpdateChallengeParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
  
    public UpdateChallengeParticipant() {
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
			
			// Cogemos el usuario
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Cogemos la historia activa en la que está participando
			List <Story> stories = daoManager.getActiveStoriesFromParticipant(user);
			if (stories.size()>1) System.out.println("El usuario participa en más de una historia en UpdateChallengeParticipant");
			Story story = stories.get(0);
			
			// Cogemos el reto activo en el que está participando
			Wrap linker = story.getChallenge();
			Challenge challenge = linker.getChallenge();
			String message = "";
			String isClosed = "false";
			
			// Si el reto está cerrado
			if (challenge.getState()==0) {
				isClosed = "true";
				
				// Si soy la historia ganadora de este reto
				if (story.getWinnerStory().getChallenge()==challenge){
					message = challenge.getWinnersMessage();
				}
				else {
					message = "Lo siento, no has podido ganar este reto... ¡Pero seguro que el próximo si! :)";
				}
				
			}
					
			// Escribimos la respuesta en JSON
			String [] array = {"isClosed", isClosed, "message", message};
			String responseJSON = JSONParser.parserJSONObjects(array);
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(responseJSON);			
			out.flush();
			out.close();	
		}
		
		// Si accede por /UpdateChallengeParticipant
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);		
	}
}
