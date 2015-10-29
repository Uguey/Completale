package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
import javax.transaction.UserTransaction;

import classes.JSONParser;
import daoImpl.DAOManager;
import entities.User;

/**
 * Servlet implementation class RetrieveSearchFollowers
 */
@WebServlet("/RetrieveSearchFollowers")
public class RetrieveSearchFollowers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public RetrieveSearchFollowers() {
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
		
		// Si tiene sesión
		if (username!=null){
			
			// Obtenemos el usuario que busca 
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveSearch");
			User user =null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos la búsqueda y los seguidores elegidos
			String search = (String) request.getParameter("search");
			String followersAlreadyChosen = (String) request.getParameter("alreadyChosen");
			
			// Obtenemos los nombres de los followers que coinciden con la búsqueda	
			List <String> followersName = daoManager.getFollowersWithPartOfTheName(user.getNick(), search);
			
			// Obtenemos los followers a partir de los nombres
			List <User> followers = new ArrayList <User>();
			Iterator <String> followersNameIterator = followersName.iterator();
			while(followersNameIterator.hasNext()){
				String currentFollowerName = followersNameIterator.next();
				users = daoManager.getUser(currentFollowerName);
				if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveSearch");
				User currentFollower = null;
				usersIterator=users.iterator();
				while(usersIterator.hasNext()==true){
					currentFollower=usersIterator.next();
				}
				followers.add(currentFollower);
			}		
			
			// Eliminamos los que ya hemos colocado
			followers = eraseUsersChosen (followers, followersAlreadyChosen);
					
			// Obtenemos los nombres de los followers
			String [] followersArray = new String [followers.size()];
			for (int i=0; i<followersArray.length; i++){
				followersArray[i] = followers.get(i).getNick();
			}
			
			// Construimos la respuesta en JSON
			String responseJSON = JSONParser.parserJSONArray(followersArray);			
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(responseJSON);			
			out.flush();
			out.close();
		}
			
		// Si accede por /CloseSession
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
	
	private List <User> eraseUsersChosen (List <User> followers, String chosen){
		String[] array = null;
		
		if (chosen.equals("")==false){
			array = chosen.split(", ");
		}
		
		if (array!=null){
			List <User> chosens = new ArrayList <User> ();
			
			for (int i=0; i<array.length; i++){
				List<User> users = daoManager.getUser(array[i]);
				if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveSearch");
				User user=null;
				Iterator<User>usersIterator=users.iterator();
				while(usersIterator.hasNext()==true){
					user=usersIterator.next();
				}
				chosens.add(user);
			}
			Iterator <User> followersIterator = followers.iterator();
			Iterator <User> chosensIterator = chosens.iterator();
			ArrayList <Integer> userToErase = new ArrayList <Integer>();
			int i=0;
			while(followersIterator.hasNext()){
				User currentFollower = followersIterator.next();
				while(chosensIterator.hasNext()){
					User currentChosen = chosensIterator.next();
					if ((currentFollower.getNick().equals(currentChosen.getNick()))
						&&(currentFollower.getEmail().equals(currentChosen.getEmail()))){
						userToErase.add(i);
					}
				}
				i++;
			}
			Iterator <Integer> userToEraseIterator = userToErase.iterator();
			while(userToEraseIterator.hasNext()){
				int remove = userToEraseIterator.next();
				followers.remove(remove);
			}
	
		}		
		return followers;
	}

}






















