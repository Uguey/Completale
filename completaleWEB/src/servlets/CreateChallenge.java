package servlets;

import java.io.IOException;
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

import classes.FillerForMainMainStoriesAndChallenges;
import daoImpl.DAOManager;
import entities.Challenge;
import entities.ChallengePK;
import entities.User;

@WebServlet("/CreateChallenge")
public class CreateChallenge extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public CreateChallenge() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		daoManager = new DAOManager (em,ut);
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		// Si  hay sesión
		if(username!=null){
			
			// Obtenemos los parámetros de la petición
			String title = (String) request.getParameter("titleChallenge");
			String description = (String) request.getParameter("descriptionChallenge");
			String winnerMessage = (String) request.getParameter("winnerMessageChallenge");
			String maxStories = (String) request.getParameter("maxStoriesChallenge");
			String endDate = (String) request.getParameter("endDateChallenge");			
			
			// Creamos el reto
			Challenge newChallenge = createChallenge(username, title, description, winnerMessage, maxStories, endDate);
			
			// Lo guardamos en la base de datos
			try {
				daoManager.insertChallenge(newChallenge);
			} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Creamos la respuesta y vamos a MainChallengesLeader
			request = FillerForMainMainStoriesAndChallenges.fillMainAndMainChallengesLeader(request, username, daoManager, newChallenge);			
			
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainChallengesLeader.jsp").forward(request, response);
		}
		
		// Si accede por /CreateChallenge
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);		
	}
	
	private Challenge createChallenge (String leader, String title, String description, String winnerMessage, String maxStories, String endDate){
		
		Challenge newChallenge = null;
		ChallengePK newChallengePK = null;		
		
		Date openDate = new Date();		
		newChallengePK = new ChallengePK (leader, openDate); 
		
		Date endDateChallenge = null;		
		if (endDate.equals("1")) endDateChallenge = new Date (openDate.getTime() + (1000*60*60*24));
		else if (endDate.equals("2")) endDateChallenge = new Date (openDate.getTime() + (1000*60*60*48));
		else if (endDate.equals("3")) endDateChallenge = new Date (openDate.getTime() + (1000*60*60*72));
		else if (endDate.equals("4")) endDateChallenge = new Date (openDate.getTime() + (1000*60*60*96));
		else if (endDate.equals("5")) endDateChallenge = new Date (openDate.getTime() + (1000*60*60*120));
		else System.out.println("Error al parsear la fecha de terminación en CreateChallenge");
		
		List<User> users = daoManager.getUser(leader);
		if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en CreateChallenge");
		User user=null;
		Iterator<User>usersIterator=users.iterator();
		while(usersIterator.hasNext()==true){
			user=usersIterator.next();
		}
		
		newChallenge = new Challenge(newChallengePK, title, description, Integer.parseInt(maxStories),
				null,(byte) 1, endDateChallenge, winnerMessage, user, null, null, null); 
		
		return newChallenge;
	}
}




















