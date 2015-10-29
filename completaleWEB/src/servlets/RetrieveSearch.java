package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import classes.LikesAndIfIFollowAllFollower;
import daoImpl.DAOManager;
import entities.User;

@WebServlet("/RetrieveSearch")
public class RetrieveSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;

    public RetrieveSearch() {
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
			
			// Obtenemos al usuario que busca 
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en RetrieveSearch");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos la búsqueda
			String search = (String) request.getParameter("search");

			// Obtenemos los usuarios que coincidan con esa búsqueda
			List <User> usersSearched = daoManager.getUserWithPartOfTheName(search);
			
			// Eliminamos al propio usuario que busca
			Iterator <User> usersSearchedIterator = usersSearched.iterator();
			int i=0;
			int positionRemove = -1;
			while(usersSearchedIterator.hasNext()==true){
				User userList = usersSearchedIterator.next();
				String nickUserList = userList.getNick();
				String emailUserList = userList.getEmail();
				if ((nickUserList.equals(user.getNick()))&&(emailUserList.equals(user.getEmail()))){
					positionRemove = i;
				}
				i++;
			}
			if (positionRemove!=-1) usersSearched.remove(positionRemove);
			
			// Eliminamos a los usuarios que ya seguimos
			List <String> followings = daoManager.getFollowings (user.getNick());
			usersSearched = this.eraseUsers (usersSearched, followings);
						
			// Eliminamos a los usuarios que ya hemos pedido una solicitud de seguimiento
			List <String> followingsRequested = daoManager.getFollowingRequests (user.getNick());
			usersSearched = this.eraseUsers (usersSearched, followingsRequested);
			
			// Obtenemos los likes de todos los usuarios buscados
			Map <User, Integer> usersSearchedLikes = LikesAndIfIFollowAllFollower.getLikes(usersSearched);
			
			// Obtenemos si el usuario que busca sigue a los usuarios buscados 
			Map <User, Boolean> usersSearchedIfIamFollowing = LikesAndIfIFollowAllFollower.getIfIFollowFollower(usersSearched, daoManager.getFollowings(user.getNick()));
			
			// Creamos la respuesta en JSON
			int arraySize = 2+(usersSearched.size()*6);
			String [] array = new String [arraySize];
			array[0] = "number";
			array[1] = Integer.toString(usersSearched.size());
			
			int namePos=0;
			int arrayPos=2;
			while (namePos<usersSearched.size()){
				array[arrayPos]="username"+namePos;
				array[arrayPos+1]=usersSearched.get(namePos).getNick().toString();
				namePos++;
				arrayPos+=2;
			}
			
			namePos=0;
			while (namePos<usersSearchedLikes.size()){
				array[arrayPos]="likes"+namePos;
				array[arrayPos+1]=usersSearchedLikes.get(usersSearched.get(namePos)).toString();
				namePos++;
				arrayPos+=2;
			}
			
			namePos=0;
			while (namePos<usersSearchedIfIamFollowing.size()){
				array[arrayPos]="following"+namePos;
				array[arrayPos+1]=usersSearchedIfIamFollowing.get(usersSearched.get(namePos)).toString();
				namePos++;
				arrayPos+=2;
			}
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(JSONParser.parserJSONObjects(array));			
			out.flush();
			out.close();
		}
		
		// Si accede por /CloseSession
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
	
	public List <User> eraseUsers (List <User> usersSearched, List <String> usersToErase){
		
		Iterator <User> usersSearchedIterator = usersSearched.iterator();
		Iterator <String> followingsIterator = usersToErase.iterator();					
		ArrayList <Integer> positionsRemove = new ArrayList <Integer>();
		
		int j=0;		
		while(usersSearchedIterator.hasNext()==true){
			User userList = usersSearchedIterator.next();
			while(followingsIterator.hasNext()==true){
				String currentFollowing = followingsIterator.next();
				if (userList.getNick().equals(currentFollowing)){
					positionsRemove.add(j); 
				}
			}
			j++;
		}
		
		Iterator <Integer> positionsRemoveIterator = positionsRemove.iterator();
		while(positionsRemoveIterator.hasNext()){
			int currentPositionToRemove = positionsRemoveIterator.next();
			usersSearched.remove(currentPositionToRemove);
		}
		
		return usersSearched;
	}
}









