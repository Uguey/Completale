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

import classes.Validation;
import daoImpl.DAOManager;
import entities.User;

/**
 * Servlet implementation class ChangeDataUser
 */
@WebServlet("/ChangeDataUser")
public class ChangeDataUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
		
    public ChangeDataUser() {
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
		// Si tiene sesión
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		if(username!=null){
			
			// Validamos los datos
			String newUsername = (String) request.getParameter("usernameDarseDeAlta");
			String newEmail = (String) request.getParameter("emailDarseDeAlta");
			String newPassword1 = (String) request.getParameter("passwordDarseDeAlta");
			String newPassword2 = (String) request.getParameter("passwordRepeatedDarseDeAlta");
			String failChangeData = Validation.validatorSingUp(newUsername, newEmail, newPassword1, newPassword2);
			if (failChangeData != null) {					
				request.setAttribute("failChangeData",failChangeData);
				this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
			}
			else {
				
				// Obtenemos el usuario
				List<User> users = daoManager.getUser(username);
				if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
				User user=null;
				Iterator<User>usersIterator=users.iterator();
				while(usersIterator.hasNext()==true){
					user=usersIterator.next();
				}
				
				// Si el nick o el email introducidos son iguales al del antiguo usuario
				if (((user.getNick().equals(newUsername)==true)||(user.getEmail().equals(newEmail)==true))
					||((user.getNick().equals(newUsername)==true)&&(user.getEmail().equals(newEmail)==true))){

					// Actualizamos el usuario en la base de datos y actualizamos la sesión
					this.updateUser(user, session, newUsername, newEmail, newPassword1);
					
					// Vamos a Initio
					this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
				}
				
				// Si el nick o el email introducidos son distintos al del antiguo usuario
				else{
					
					// Vemos si el nuevo usuario introducido ya existe
					List<User> otherUsersNick = daoManager.getUser(newUsername);
					List<User> otherUsersEmail = daoManager.getUserByEmail(newEmail);			
					if ((otherUsersNick.isEmpty()==false)||(otherUsersEmail.isEmpty()==false)
						||(otherUsersNick.isEmpty()==false)&&(otherUsersEmail.isEmpty()==false)){
						request.setAttribute("failChangeData","El usuario o email que has introducido ya existe");
						this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
					}
					
					// Si el usuario y el email son completamente nuevos
					else {
						
						// Actualizamos el usuario en la base de datos y actualizamos la sesión
						this.updateUser(user, session, newUsername, newEmail, newPassword1);
						
						// Vamos a Initio
						this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
					}
				}
			}
		}
		
		// Si accede por /ChangeDataUser
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
	
	public void updateUser (User oldUser, HttpSession session, String newUsername, String newEmail, String newPassword1) {
		
		// Establecemos los nuevos valores del nuevo usuario
		User newUser = oldUser;
		newUser.setNick(newUsername);
		newUser.setEmail(newEmail);
		newUser.setPassword(newPassword1);
	
		// Lo guardamos en la base de datos
		try {
			daoManager.mergeUserChangingNick(newUser, (String) session.getAttribute("username"));
		} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException
				| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		
		// Actualizamos la sesión
		session.setAttribute("username", newUser.getNick());
	}
}











