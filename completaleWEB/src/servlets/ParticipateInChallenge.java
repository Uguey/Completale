package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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

import classes.FillStory;
import classes.FillerForMainMainStoriesAndChallenges;
import daoImpl.DAOManager;
import entities.Challenge;
import entities.Story;
import entities.User;
import entities.Wrap;
import entities.WrapPK;

@WebServlet("/ParticipateInChallenge")
public class ParticipateInChallenge extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public ParticipateInChallenge() {
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
			
			// Obtenemos los parámetros de la petición
			String titleChallengeWithManager = (String) request.getParameter("title");
			String managerChallengeString="";
			if ((titleChallengeWithManager.equals("")==false)&&(titleChallengeWithManager!=null)){
				String [] titleChallengeWithManagerArray = titleChallengeWithManager.split(":");
				managerChallengeString=titleChallengeWithManagerArray[1].trim();
			}
			
			String titleStory = (String) request.getParameter("titleStory");
			int maxNumberOfParticipants=0;
			if (request.getParameter("maxParticipantsStory")!=null){
				maxNumberOfParticipants = Integer.parseInt((String) request.getParameter("maxParticipantsStory"));
			}			
			Enumeration <String> parameters = request.getParameterNames();
			ArrayList <String> participants = new ArrayList <String>();
			while (parameters.hasMoreElements()){
				String currentParameter = parameters.nextElement();
				if ((currentParameter.equals("titleStory"))||(currentParameter.equals("maxParticipantsStory")
						||currentParameter.equals("title"))) continue;
				else participants.add((String)request.getParameter(currentParameter));
			}
			
			// Creamos la historia
			Story newStory = FillStory.createStory (username, titleStory, maxNumberOfParticipants, participants, daoManager);
			if (newStory==null) {
				request.setAttribute("failCreateStory", "Los participantes introducidos no existen, por favor introduce sólo los que te sugiere la aplicación");
				this.getServletConfig().getServletContext().getRequestDispatcher("/DataCharger").forward(request, response);
			}
			else {				
			
				// Enlazamos la historia con el reto
				
				// Primero obtenemos el reto
				List<User> managerChallenges = daoManager.getUser(managerChallengeString);
				if(managerChallenges.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en ParticipateInChallenge");
				User managerChallenge = managerChallenges.get(0);	
				
				List <Challenge> challenges = daoManager.getActiveChallengesFromManager(managerChallenge);
				if(challenges.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en ParticipateInChallenge");
				Challenge challenge = challenges.get(0);
				
				// Lo enlazamos con la historia
				WrapPK linkerPK = new WrapPK(newStory.getId().getLeader(), newStory.getId().getOpenDate());
				Wrap linker = new Wrap (linkerPK, challenge, newStory);			
				List <Wrap> stories = challenge.getStories();
				stories.add(linker);
				newStory.setChallenge(linker);
				
				try {
					daoManager.insertStory(newStory);
					daoManager.mergeChallenge(challenge);
				} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
						| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Creamos los parámetros de salida
				request = FillerForMainMainStoriesAndChallenges.fillMainAndMainChallengesParticipant(request, username, daoManager, challenge, newStory);
	 						    	
				// Redirigimos a MainChallengesParticipant
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainChallengesParticipant.jsp").forward(request, response);
			}
			
		}
		
		// Si accede por /ParticipateInChallenge
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);	
	}

}














