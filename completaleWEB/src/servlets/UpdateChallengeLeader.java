package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import entities.Challenge;
import entities.Story;
import entities.User;
import entities.Winnerstory;
import entities.WinnerstoryPK;
import entities.Wrap;

@WebServlet("/UpdateChallengeLeader")
public class UpdateChallengeLeader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public UpdateChallengeLeader() {
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
			
			// Cogemos su reto activo
			List <Challenge> challenges = daoManager.getActiveChallengesFromManager(user);
			if (challenges.size()>1) System.out.println("El manager tiene más de un reto activo en UpdateChallengeLeader");
			Challenge challenge = challenges.get(0);
			
			// Verificamos la fecha del reto
			Date endDate = challenge.getWinnersElectionDate();
			Date now = new Date();
			String message = "";
			String isClosed = "false";
			
			// Si es la fecha actual
			if (now.compareTo(endDate)>=0) {
				
				// Elegimos los ganadores
				challenge = this.chooseWinners(challenge);		
				
				// Cerramos el reto
				byte state = challenge.getState();
				if (state==1) state=0;
				else System.out.println("El reto no debería estar abierto en UpdateChallengeLeader");
				challenge.setState(state);
				
				// Lo guardamos en la base de datos
				try {
					daoManager.mergeChallenge(challenge);
				} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
						| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Escribimos un mensaje de cierre y ponemos el boolean a true
				message = "El reto ha terminado, así que hemos enviado tu mensaje al/los participantes que tienen más likes, ¡Muchas gracias!";
				isClosed = "true";
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
		
		// Si accede por /UpdateChallengeLeader
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);		
	}
	
	public Challenge chooseWinners (Challenge challenge) {
		
		// Obtenemos todas las historias del reto
		List <Wrap> linker = challenge.getStories();
		List <Story> stories = new ArrayList <Story>();
		Iterator <Wrap> linkerIterator = linker.iterator();
		while (linkerIterator.hasNext()){
			stories.add(linkerIterator.next().getStory());
		}
		
		// Si el reto tiene historias
		if (stories.isEmpty()==false){
			
			// Seteamos el número máximo de likes
			int maxLikes = 0;
			Iterator <Story> storiesIterator = stories.iterator();
			while (storiesIterator.hasNext()){
				Story currentStory = storiesIterator.next();
				if (currentStory.getLikes()>maxLikes) {
					maxLikes = currentStory.getLikes();
				}
			}
			
			// Buscamos aquellas con el número máximo de likes, las seteamos como historias ganadoras
				// y la añadimos al reto
			storiesIterator = stories.iterator();
			while (storiesIterator.hasNext()){
				Story winnerStory = storiesIterator.next();
				if (winnerStory.getLikes()==maxLikes) {
					
					WinnerstoryPK linkerWinnerStoryPK = new WinnerstoryPK 
							(winnerStory.getId().getLeader(), winnerStory.getId().getOpenDate());
					Winnerstory linkerWinnerStory = new Winnerstory (linkerWinnerStoryPK, challenge, winnerStory);
					
					List <Winnerstory> winnerStoriesChallenge = challenge.getWinnerStories();
					winnerStoriesChallenge.add(linkerWinnerStory);
					challenge.setWinnerStories(winnerStoriesChallenge);
					
					winnerStory.setWinnerStory(linkerWinnerStory);
					
					// Mergeamos la historia en la base de datos
					try {
						daoManager.mergeStory(winnerStory);
					} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
							| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				else if (winnerStory.getLikes()>maxLikes) {
					System.out.println("Ha habido algún problema a la hora de elegir los ganadores en UpdateChallengeLeader");
				}							
			}		
		}
		
		return challenge;
	}	
}






















