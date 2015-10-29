package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

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
import entities.Story;

@WebServlet("/CreateStory")
public class CreateStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
    
    private String failChangeData=null;

    public CreateStory() {
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
			
			// Obtenemos los parámetros de la historia y de las solicitudes de participación
			String title = (String) request.getParameter("titleStory");
			int maxNumberOfParticipants=0;
			if (request.getParameter("maxParticipantsStory")!=null){
				maxNumberOfParticipants = Integer.parseInt((String) request.getParameter("maxParticipantsStory"));
			}			
			Enumeration <String> parameters = request.getParameterNames();
			ArrayList <String> participants = new ArrayList <String>();
			while (parameters.hasMoreElements()){
				String currentParameter = parameters.nextElement();
				if ((currentParameter.equals("titleStory"))||(currentParameter.equals("maxParticipantsStory"))) continue;
				else participants.add((String)request.getParameter(currentParameter));
			}
							
			// Creamos la historia
			Story newStory = FillStory.createStory (username, title, maxNumberOfParticipants, participants, daoManager);
			if (newStory==null) {
				request.setAttribute("failCreateStory", "Los participantes introducidos no existen, por favor introduce sólo los que te sugiere la aplicación");
				this.getServletConfig().getServletContext().getRequestDispatcher("/DataCharger").forward(request, response);
			}
			else {
				try {
					daoManager.insertStory(newStory);
				} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
						| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Colocamos los datos del usuario para mainStories
				request = FillerForMainMainStoriesAndChallenges.retrieveAndSetData(request, username, daoManager);
							
				// Ponemos el título como parámetro de salida
				request.setAttribute("Title", newStory.getTitle());			
				
				// Llenamos con los datos necesarios			
				if (request.getAttribute("failChangeData")!=null) failChangeData = (String) request.getAttribute("failChangeData");	
				if (failChangeData!=null)request.setAttribute("failChangeData", failChangeData);
				
				// Reenviamos a mainStories			
				this.getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/MainStories.jsp").forward(request, response);
			}
		}
		
		// Si accede por /CreateStory
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);		
	}
}














