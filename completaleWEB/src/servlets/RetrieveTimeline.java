package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.transaction.UserTransaction;

import classes.JSONParser;
import daoImpl.DAOManager;
import entities.Challenge;
import entities.ChallengePK;
import entities.Story;
import entities.StoryPK;
import entities.User;
import entities.Wrap;

@WebServlet("/RetrieveTimeline")
public class RetrieveTimeline extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="completaleJPA")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private DAOManager daoManager;
       
    public RetrieveTimeline() {
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
			
			// Obtenemos el usuario
			List<User> users = daoManager.getUser(username);
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			User user=null;
			Iterator<User>usersIterator=users.iterator();
			while(usersIterator.hasNext()==true){
				user=usersIterator.next();
			}
			
			// Obtenemos a los usuarios a los que sigue
			List <String> followingsName = daoManager.getFollowings(user.getNick());
			List <User> followings = new ArrayList <User>();

			Iterator<String>followingsNameIterator=followingsName.iterator();
			while(followingsNameIterator.hasNext()==true){
				String currentFollowingName = followingsNameIterator.next();
				
				List <User> currentFollowing = daoManager.getUser(currentFollowingName);
				if(currentFollowing.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
				User currentUser=null;
				Iterator<User>currentFollowingIterator=currentFollowing.iterator();
				while(currentFollowingIterator.hasNext()==true){
					currentUser=currentFollowingIterator.next();
				}
				
				followings.add(currentUser);				
			}
						
			// Obtenemos toda la lista de historias de los usuarios a los que sigue
			List <Story> stories = this.retrieveStories (followings);
			
			// Eliminamos las historias en las que ya estamos participando
			stories = this.eraseStoriesInWhichIParticipate(stories, user);
						
			// Ordenamos la lista por fecha
			stories = this.sortStories (stories);
			
			// Metemos los parámetros importantes de las historias en un array
			String [][] arrayStories = retrieveArrayStories (stories);
			
			// Obtenemos toda la lista de retos de los usuarios a los que sigue
			List <Challenge> challenges = this.retrieveChallenges (followings);
			
			// Eliminamos los retos en las que ya estamos participando
			challenges = this.eraseChallengesInWhichIParticipate(challenges, user);
			
			// Eliminamos los retos que tengan más historias que su máximo
			challenges = this.eraseChallengesWithLimitOverCome(challenges);
			
			// Eliminamos los retos que el usuario ha creado
			challenges = this.eraseChallengesInWhichIAmTheManager(challenges, user);
			
			// Eliminamos los retos que están cerrados
			challenges = this.eraseChallengesClosed(challenges);
						
			// Ordenamos la lista por fecha
			challenges = this.sortChallenges (challenges);
			
			// Metemos los parámetros importantes de los retos en un array
			String [][] arrayChallenges =  this.retrieveArrayChallenges (challenges);
			
			// Unimos ambos arrays
			String [][] array = this.unifyArrays(arrayStories, arrayChallenges);
			
			// Construimos el array JSON
			String storiesReady = JSONParser.parserJSONArrays(array);
			
			// Enviamos la respuesta
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(storiesReady);			
			out.flush();
			out.close();
				
		}
		
		// Si accede por /RetrieveTimeline
		else this.getServletConfig().getServletContext().getRequestDispatcher("").forward(request, response);
	}
	
	public List <Story> retrieveStories (List <User> followings){
		
		List <Story> stories = new ArrayList <Story>();
		Iterator <User> followingsIterator = followings.iterator();
		while (followingsIterator.hasNext()){
			User currentFollowing = followingsIterator.next();
			List <Story> storiesCurrentFollowing = daoManager.getStoriesFromParticipant(currentFollowing);
			Iterator <Story> storiesCurrentFollowingIterator = storiesCurrentFollowing.iterator();
			while (storiesCurrentFollowingIterator.hasNext()){
				stories.add(storiesCurrentFollowingIterator.next());
			}
		}
		
		return stories;
	}
	
	public List <Story> eraseStoriesInWhichIParticipate (List<Story> stories, User user){
		
		List <Story> storiesInWhichIParticipated = daoManager.getActiveStoriesFromParticipant(user);
		
		// Eliminamos a las historias que están cerradas		
		Iterator <Story> storiesInWhichIParticipatedIterator = storiesInWhichIParticipated.iterator();		
		ArrayList <Integer> removeStoriesClosed = new ArrayList <Integer>();
		int i=0;
		while(storiesInWhichIParticipatedIterator.hasNext()){
			Story currentStory = storiesInWhichIParticipatedIterator.next();
			if (currentStory.getState()==0){
				removeStoriesClosed.add(i);
			}
			i++;
		}		
		Iterator <Integer> removeStoriesClosedIterator = removeStoriesClosed.iterator();
		while(removeStoriesClosedIterator.hasNext()){
			int removeStory = removeStoriesClosedIterator.next();
			storiesInWhichIParticipated.remove(removeStory);
		}
		
		// Comparamos las historias abiertas con las de los followers y borramos si hay match
		Iterator <Story> storiesIterator = stories.iterator();
		storiesInWhichIParticipatedIterator = storiesInWhichIParticipated.iterator();	
		ArrayList <Integer> removeStory = new ArrayList <Integer>();
		i=0;
		while(storiesIterator.hasNext()){
			Story storyFollower = storiesIterator.next();
			StoryPK storyFollowerPK = storyFollower.getId();
			while(storiesInWhichIParticipatedIterator.hasNext()){
				Story myCurrentStory = storiesInWhichIParticipatedIterator.next();
				StoryPK myCurrentStoryPK = myCurrentStory.getId();
				
				if ((storyFollowerPK.getLeader().equals(myCurrentStoryPK.getLeader()))&&
					(storyFollowerPK.getOpenDate()==myCurrentStoryPK.getOpenDate())){
					removeStory.add(i);	
				}				
			}
			i++;
		}
		Iterator <Integer> removeStoryIterator = removeStory.iterator();
		while(removeStoryIterator.hasNext()){
			int remove = removeStoryIterator.next();
			stories.remove(remove);
		}
		
		return stories;
	}
	
	public List <Story> sortStories (List <Story> stories) {
				
		for(int i=0;i<stories.size();i++){
			int j=0;
			while(j+1<stories.size()){
				
				Story currentStory = stories.get(j);
				StoryPK currentStoryPK = currentStory.getId();
				Date currentDate = currentStoryPK.getOpenDate();
								
				Story nextStory = stories.get(j+1);
				StoryPK nextStoryPK = nextStory.getId();
				Date nextDate = nextStoryPK.getOpenDate();
								
				if (nextDate.compareTo(currentDate)>=0){
					stories.set(j, nextStory);
					stories.set(j+1, currentStory);
					j+=2;
				}
			}
		}
		
		return stories;
	}	
	
	public String [][] retrieveArrayStories (List <Story> stories){
		String [][] array = new String [stories.size() + 1][6];

		array [0][0] = Integer.toString(stories.size() + 1);
		
		Iterator <Story> storiesIterator = stories.iterator();
		int i=1;
		while(storiesIterator.hasNext()){
			Story currentStory = storiesIterator.next();
			
			array[i][0] = currentStory.getTitle();
			array[i][1] = Integer.toString(currentStory.getUsersParticipating().size());
			array[i][2] = Byte.toString(currentStory.getMaxNumberOfParticipants());
			array[i][3] = Integer.toString(currentStory.getLikes());
			if (currentStory.getState()==1)	array[i][4] ="Abierta";
			else if (currentStory.getState()==0) array[i][4] ="Cerrada";
			else System.out.println("Problema en RetrieveTimeline");		
			
			i++;
		}
		
		return array;		
	}
	
	public List <Challenge> retrieveChallenges (List <User> followings){
		
		List <Challenge> challenges = new ArrayList <Challenge>();
		
		Iterator <User> followingsIterator = followings.iterator();
		while (followingsIterator.hasNext()){
			User currentFollowing = followingsIterator.next();
			List <Challenge> challengesCurrentFollowing = daoManager.getActiveChallengesFromManager(currentFollowing);
			Iterator <Challenge> challengesCurrentFollowingIterator = challengesCurrentFollowing.iterator();
			while (challengesCurrentFollowingIterator.hasNext()){
				challenges.add(challengesCurrentFollowingIterator.next());
			}
		}
		
		return challenges;
	}
	
	public List <Challenge> eraseChallengesInWhichIParticipate (List<Challenge> challenges, User user){
		
		List <Story> storiesInWhichIParticipated = daoManager.getActiveStoriesFromParticipant(user);
		
		// Eliminamos a las historias que están cerradas		
		Iterator <Story> storiesInWhichIParticipatedIterator = storiesInWhichIParticipated.iterator();		
		ArrayList <Integer> removeStoriesClosed = new ArrayList <Integer>();
		int i=0;
		while(storiesInWhichIParticipatedIterator.hasNext()){
			Story currentStory = storiesInWhichIParticipatedIterator.next();
			if (currentStory.getState()==0){
				removeStoriesClosed.add(i);
			}
			i++;
		}		
		Iterator <Integer> removeStoriesClosedIterator = removeStoriesClosed.iterator();
		while(removeStoriesClosedIterator.hasNext()){
			int removeStory = removeStoriesClosedIterator.next();
			storiesInWhichIParticipated.remove(removeStory);
		}
		
		// Obtenemos los retos de las historias en las que estamos participado que están abiertas 
		 // y los eliminamos de los retos de los usuarios a los que seguimos
		Iterator <Challenge> challengesFollowersIterator = challenges.iterator();
		storiesInWhichIParticipatedIterator = storiesInWhichIParticipated.iterator();	
		ArrayList <Integer> removeChallenge = new ArrayList <Integer>();
		i=0;
		while(challengesFollowersIterator.hasNext()){
			Challenge currentChallengeFollowing = challengesFollowersIterator.next();
			ChallengePK currentChallengeFollowingPK = currentChallengeFollowing.getId();
			
			while(storiesInWhichIParticipatedIterator.hasNext()) {	
				
				Wrap challengeInWhichIParticipatedWrap = storiesInWhichIParticipatedIterator.next().getChallenge();
				if (challengeInWhichIParticipatedWrap!=null){					
					Challenge challengeInWhichIParticipated = challengeInWhichIParticipatedWrap.getChallenge();
					ChallengePK challengeInWhichIParticipatedPK = challengeInWhichIParticipated.getId();
					
					if ((currentChallengeFollowingPK.getLeader().equals(challengeInWhichIParticipatedPK.getLeader()))
						&& (currentChallengeFollowingPK.getOpenDate()==challengeInWhichIParticipatedPK.getOpenDate())){
						removeChallenge.add(i);
					}
				}				
			}
			i++;
		}	
		Iterator <Integer> removeChallengeIterator = removeChallenge.iterator();
		while(removeChallengeIterator.hasNext()){
			int remove = removeChallengeIterator.next();
			challenges.remove(remove);
		}		
		
		return challenges;
	}
	
	public List <Challenge> sortChallenges (List <Challenge> challenges) {
				
		for(int i=0;i<challenges.size();i++){
			int j=0;
			while(j+1<challenges.size()){
				
				Challenge currentChallenge = challenges.get(j);
				ChallengePK currentChallengePK = currentChallenge.getId();
				Date currentDate = currentChallengePK.getOpenDate();
								
				Challenge nextChallenge = challenges.get(j+1);
				ChallengePK nextChallengePK = nextChallenge.getId();
				Date nextDate = nextChallengePK.getOpenDate();
								
				if (nextDate.compareTo(currentDate)>=0){
					challenges.set(j, nextChallenge);
					challenges.set(j+1, currentChallenge);
					j+=2;
				}
			}
		}
		
		return challenges;
	}		
	
	public String [][] retrieveArrayChallenges (List <Challenge> challenges){
		String [][] array = new String [challenges.size()][6];
		
		Iterator <Challenge> challengesIterator = challenges.iterator();
		
		DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		int i=0;
		while(challengesIterator.hasNext()){
			Challenge currentChallenge = challengesIterator.next();
			ChallengePK currentChallengePK = currentChallenge.getId();
			
			array[i][0] = currentChallenge.getTitle();
			array[i][1] = currentChallengePK.getLeader();
			array[i][2] = Integer.toString(currentChallenge.getStories().size());
			array[i][3] = Integer.toString(currentChallenge.getMaxNumberOfStories());
			array[i][4] = dateFormater.format(currentChallenge.getWinnersElectionDate());
			if (currentChallenge.getState()==1)	array[i][5] ="Abierto";
			else if (currentChallenge.getState()==0) array[i][5] ="Cerrado";
			else System.out.println("Problema en RetrieveTimeline");		
			
			i++;
		}
		
		return array;		
	}
	
	public String [][] unifyArrays (String [][] array1, String [][] array2){
		String [][] array = new String [array1.length + array2.length] [array1[0].length];
		
		for (int i=0; i<array1.length; i++){
			for (int j=0; j<array1[0].length; j++){
				array[i][j] = array1[i][j];
			}
		}
		
		for (int i=0; i<array2.length; i++){
			for (int j=0; j<array2[0].length; j++){
				array[i+(array1.length)][j] = array2[i][j];
			}
		}
		
		return array;		
	}
	
	public List <Challenge> eraseChallengesWithLimitOverCome(List <Challenge> challenges) {
		
		Iterator <Challenge> challengesIterator = challenges.iterator();
		ArrayList <Integer> remove = new ArrayList <Integer>();
		int i=0;
		while (challengesIterator.hasNext()){
			Challenge currentChallenge = challengesIterator.next();
			
			List <Wrap> stories =  currentChallenge.getStories();
			if (stories.size()>currentChallenge.getMaxNumberOfStories()){
				remove.add(i);
			}
			i++;
		}
		
		Iterator <Integer> removeIterator = remove.iterator();
		while (removeIterator.hasNext()) challenges.remove(removeIterator.next());
		
		return challenges;
	}
	
	public List <Challenge> eraseChallengesInWhichIAmTheManager(List <Challenge> challenges, User user) {
		
		Iterator <Challenge> challengesIterator = challenges.iterator();
		ArrayList <Integer> remove = new ArrayList <Integer>();
		int i=0;
		while (challengesIterator.hasNext()){
			Challenge currentChallenge = challengesIterator.next();
			
			if (user.getNick().equals(currentChallenge.getManager().getNick())){
				remove.add(i);
			}
			i++;
		}
		
		Iterator <Integer> removeIterator = remove.iterator();
		while (removeIterator.hasNext()) challenges.remove(removeIterator.next());
		
		return challenges;
	}
	
	public List <Challenge> eraseChallengesClosed(List <Challenge> challenges) {
		
		Iterator <Challenge> challengesIterator = challenges.iterator();
		ArrayList <Integer> remove = new ArrayList <Integer>();
		int i=0;
		while (challengesIterator.hasNext()){
			Challenge currentChallenge = challengesIterator.next();
			
			if (currentChallenge.getState()==0){
				remove.add(i);
			}
			i++;
		}
		
		Iterator <Integer> removeIterator = remove.iterator();
		while (removeIterator.hasNext()) challenges.remove(removeIterator.next());
		
		return challenges;
	}
}






