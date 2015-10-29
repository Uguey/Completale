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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import classes.FillerForMainMainStoriesAndChallenges;
import classes.Validation;
import daoImpl.DAOManager;
import entities.Challenge;
import entities.Story;
import entities.User;
import entities.Wrap;

/**
 * Servlet implementation class DataChargerServlet
 */
@WebServlet("/DataCharger")
public class DataCharger extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
    
    private String failChangeData=null;
    private String failCreateStory=null;
    
    public DataCharger() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		daoManager = new DAOManager (em,ut);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getAttribute("failChangeData")!=null) failChangeData=(String)request.getAttribute("failChangeData");	
		if (request.getAttribute("failCreateStory")!=null) failCreateStory=(String)request.getAttribute("failCreateStory");
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
			
			// Redirigimos a donde sea
			this.redirector (user, request, response);			
		}
		
		// Si no hay sesión
		else {
			String logIn = (String) request.getParameter("LogIn");
			String signUp = (String) request.getParameter("SignUp");
			
			// Si entra por /DataCharger
			if ((logIn==null) && (signUp==null)) this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
			
			// Si entra por el registro
			else if ((logIn==null) && (signUp.equals("SignUp")==true)) {				
				
				// Validación
				username = (String) request.getParameter("usernameDarseDeAlta");	
				String email = (String) request.getParameter("emailDarseDeAlta");			
				String password1 = (String) request.getParameter("passwordDarseDeAlta");				
				String password2 = (String) request.getParameter("passwordRepeatedDarseDeAlta");				
				String fail = Validation.validatorSingUp(username, email, password1, password2);
				if (fail != null) {					
					request.setAttribute("fail",fail);
					this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
				}
				
				else {
					User user = new User(username, email, password1, null, null, null, null, null, null, null, null, null);
					
					// Se busca si el usuario ya existe
					List<User> otherUsersNick = daoManager.getUser(user.getNick());
					List<User> otherUsersEmail = daoManager.getUserByEmail(user.getNick());
					if ((otherUsersNick.isEmpty()==false)||(otherUsersEmail.isEmpty()==false)
						||(otherUsersNick.isEmpty()==false)&&(otherUsersEmail.isEmpty()==false)){
						request.setAttribute("fail","El usuario o email que has introducido ya existe");
						this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
					}
					
					else {
						// Insertamos el usuario en la BBDD
						try {
							daoManager.insertUser(user);
						} 
						catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
								| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
							e.printStackTrace();
						}
						
						// Recuperamos sus datos
						request = FillerForMainMainStoriesAndChallenges.retrieveAndSetData(request, user.getNick(), daoManager);						
						
						// Establecemos la sesión
						stablishSession (session, request, user.getNick());
						
						// Reenviamos a main
						this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/Main.jsp").forward(request, response);
					}	
				}			
			}
			
			// Si entra por el login
			else if ((logIn.equals("LogIn")==true) && (signUp==null)) {
				
				// Validación
				username = (String) request.getParameter("usernameInicioSesion");				
				String password = (String) request.getParameter("passwordInicioSesion");
				String fail =  Validation.validatorLogIn(username, password);
				if (fail != null) {					
					request.setAttribute("fail",fail);
					this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
				}
				else {
					User user = new User(username, null, password, null, null, null, null, null, null, null, null, null);
					
					// Se busca si el usuario ya existe
					List<User> users = daoManager.matchUser(user.getNick(), user.getPassword());
					if (users.isEmpty()==true) {
						request.setAttribute("fail","El usuario o email que has introducido no existen");
						this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
					}
					else if (users.size()>1)System.out.println("Error: Hay más de un usuario con un sólo email y password en DataCharger");
					else{
						
						// Se recuperan los datos del usuario
						Iterator<User>usersIterator=users.iterator();
						while(usersIterator.hasNext()){
							user=usersIterator.next();
						}
						
						// Establecemos la sesión
						stablishSession (session, request, user.getNick());
						
						// Redirigimos a donde sea
						this.redirector (user, request, response);		
					}
				}				
			}
			
			// En cualquier otro caso
			else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
		}
	}	
	
	
	
	public void stablishSession (HttpSession session, HttpServletRequest request, String nick){
		
		session.setAttribute("username", nick);
		String rememberMe = (String) request.getParameter("rememberMe");
		if(rememberMe!=null)session.setAttribute("rememberMe", true);
	}
	
	
	
	
	public void redirector (User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		List <Story> stories = daoManager.getActiveStoriesFromParticipant(user);
		List <Challenge> challengesIManage = daoManager.getActiveChallengesFromManager(user);
		
		// Si no participamos en historias ni retos 
		if ((stories.isEmpty()==true)&&(challengesIManage.isEmpty()==true)) {
			
			// Colocamos los datos del usuario para Main
			request = FillerForMainMainStoriesAndChallenges.retrieveAndSetData(request, user.getNick(), daoManager);
			
			if (request.getAttribute("failChangeData")!=null) failChangeData=(String) request.getAttribute("failChangeData");	
			if (failChangeData!=null) {
				request.setAttribute("failChangeData", failChangeData);	
				failChangeData=null;
			}
			
			if (request.getAttribute("failCreateStory")!=null) failCreateStory=(String) request.getAttribute("failCreateStory");	
			if (failCreateStory!=null) {
				request.setAttribute("failCreateStory", failCreateStory);	
				failCreateStory=null;
			}
			
			// Reenviamos a main
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/Main.jsp").forward(request, response);
		}
		
		// Si participamos en una historia
		else if (stories.isEmpty()==false) {
			
			if (stories.size()>1) System.out.println("El usuario tiene más de una historia abierta en DataCharger");
			Story story = stories.get(0);
			
			Wrap ifStoryIsInChallengeWrap = story.getChallenge();
			
			// Si nuestra historia no está en ningún reto
			if (ifStoryIsInChallengeWrap==null){
				
				// Colocamos los datos del usuario para mainStories
				request = FillerForMainMainStoriesAndChallenges.fillMainAndMainStories(request, user.getNick(), story, daoManager);			
				
				if (request.getAttribute("failChangeData")!=null) failChangeData=(String) request.getAttribute("failChangeData");	
				if (failChangeData!=null) {
					request.setAttribute("failChangeData", failChangeData);	
					failChangeData=null;
				}	
				
				// Reenviamos a mainStories			
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainStories.jsp").forward(request, response);
			}	
			
			// Si nuestra historia está en un reto 
			else {
				
				Challenge ifStoryIsInChallenge = ifStoryIsInChallengeWrap.getChallenge();
				
				// Colocamos los datos del usuario para mainStories
				request = FillerForMainMainStoriesAndChallenges.fillMainAndMainChallengesParticipant
						(request, user.getNick(), daoManager, ifStoryIsInChallenge, story);
				
				if (request.getAttribute("failChangeData")!=null) failChangeData=(String) request.getAttribute("failChangeData");	
				if (failChangeData!=null) {
					request.setAttribute("failChangeData", failChangeData);	
					failChangeData=null;
				}
				
				// Reenviamos a mainChallengesParticipant			
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainChallengesParticipant.jsp").forward(request, response);
			}
			
		}
		
		// Si hemos creado un reto
		else if (challengesIManage.isEmpty()==false){
			
			if (challengesIManage.size()>1) System.out.println("El usuario tiene más de una reto que gestiona abierto en DataCharger");
			Challenge challengeIManage = challengesIManage.get(0);
			
			// Colocamos los datos del usuario para mainChallengesLeader
			request = FillerForMainMainStoriesAndChallenges.fillMainAndMainChallengesLeader(request, user.getNick(), daoManager, challengeIManage);		
			
			if (request.getAttribute("failChangeData")!=null) failChangeData=(String) request.getAttribute("failChangeData");	
			if (failChangeData!=null) {
				request.setAttribute("failChangeData", failChangeData);	
				failChangeData=null;
			}
			
			// Reenviamos a mainChallengesLeader		
			this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainChallengesLeader.jsp").forward(request, response);
		}
		
	}
}
