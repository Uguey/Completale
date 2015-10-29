package classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import daoImpl.DAOManager;
import entities.Challenge;
import entities.Fragment;
import entities.Story;
import entities.User;

public class FillerForMainMainStoriesAndChallenges {	
	
	public static HttpServletRequest retrieveAndSetData (HttpServletRequest request, String nick, DAOManager daoManager){
		
		List<User> users = daoManager.getUser(nick);
		if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
		User user=null;
		Iterator<User>usersIterator=users.iterator();
		while(usersIterator.hasNext()==true){
			user=usersIterator.next();
		}
		
		FillDataMain fillerDataForMain = new FillDataMain (user, daoManager);
		
		request.setAttribute("username", fillerDataForMain.getNick());
		request.setAttribute("Likes", fillerDataForMain.getLikes());
		request.setAttribute("Followers", fillerDataForMain.getFollowers());
		request.setAttribute("followedBy", daoManager.getFollowings(user.getNick()).size());
		request.setAttribute("followingRequests", fillerDataForMain.getFollowingRequests());
		request.setAttribute("storyParticipationRequests", fillerDataForMain.getStoryParticipationRequests());
		
		return request;
		
	}
	
	
	
	
	public static HttpServletRequest fillParametersForStories (HttpServletRequest request, Story story, DAOManager daoManager){
		
		// Recuperamos los datos de la historia 
		String title = story.getTitle();
		List <Fragment> fragments = daoManager.getFragmentsOrdered(story);
		List <String> fragmentsString = new ArrayList <String>();
		Iterator <Fragment> fragmentsIterator = fragments.iterator();
		while (fragmentsIterator.hasNext()){
			fragmentsString.add(fragmentsIterator.next().getFragment());
		}
							
		// Creamos la respuesta
		request.setAttribute ("Title", title);
		request.setAttribute ("Fragments", fragments);
		
		return request;
	}
	
	
	
	
	public static HttpServletRequest fillMainAndMainStories (HttpServletRequest request, String nick, Story story, DAOManager daoManager){
		
		// Recuperamos los datos de la historia 
		String title = story.getTitle();
		List <Fragment> fragments = daoManager.getFragmentsOrdered(story);
		List <String> fragmentsString = new ArrayList <String>();
		Iterator <Fragment> fragmentsIterator = fragments.iterator();
		while (fragmentsIterator.hasNext()){
			fragmentsString.add(fragmentsIterator.next().getFragment());
		}
							
		// Creamos la respuesta
		request.setAttribute ("Title", title);
		request.setAttribute ("Fragments", fragmentsString);
		
		// Creamos la respuesta de Main
		List<User> users = daoManager.getUser(nick);
		if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
		User user=null;
		Iterator<User>usersIterator=users.iterator();
		while(usersIterator.hasNext()==true){
			user=usersIterator.next();
		}
		
		FillDataMain fillerDataForMain = new FillDataMain (user, daoManager);
		
		request.setAttribute("username", fillerDataForMain.getNick());
		request.setAttribute("Likes", fillerDataForMain.getLikes());
		request.setAttribute("Followers", fillerDataForMain.getFollowers());
		request.setAttribute("followedBy", daoManager.getFollowings(user.getNick()).size());
		request.setAttribute("followingRequests", fillerDataForMain.getFollowingRequests());
		request.setAttribute("storyParticipationRequests", fillerDataForMain.getStoryParticipationRequests());
		
		return request;
	}
	
	
	
	
	public static HttpServletRequest fillMainChallengesLeader (HttpServletRequest request, Challenge newChallenge){
		
		request.setAttribute("title", newChallenge.getTitle());
		
		FillDataMainChallengeLeader fillDataMainChallengeLeader = new FillDataMainChallengeLeader (newChallenge);
			
		request.setAttribute("titleStories", fillDataMainChallengeLeader.getTitles());
		request.setAttribute("participantStories", fillDataMainChallengeLeader.getParticipantStories());
		request.setAttribute("maxParticipantStories", fillDataMainChallengeLeader.getMaxParticipantStories());
		request.setAttribute("likes", fillDataMainChallengeLeader.getLikes());
		request.setAttribute("state", fillDataMainChallengeLeader.getState());
		
		return request;
	}
	
	
	
	public static HttpServletRequest fillMainAndMainChallengesLeader (HttpServletRequest request, String nick, DAOManager daoManager, Challenge challenge){
		
		request = FillerForMainMainStoriesAndChallenges.retrieveAndSetData(request,nick,daoManager);
		
		request.setAttribute("title", challenge.getTitle());
		
		FillDataMainChallengeLeader fillDataMainChallengeLeader = new FillDataMainChallengeLeader (challenge);
			
		request.setAttribute("titleStories", fillDataMainChallengeLeader.getTitles());
		request.setAttribute("participantStories", fillDataMainChallengeLeader.getParticipantStories());
		request.setAttribute("maxParticipantStories", fillDataMainChallengeLeader.getMaxParticipantStories());
		request.setAttribute("likes", fillDataMainChallengeLeader.getLikes());
		request.setAttribute("state", fillDataMainChallengeLeader.getState());
		
		return request;
		
	}
	
	
	
	
	public static HttpServletRequest fillMainAndMainChallengesParticipant 
			(HttpServletRequest request, String nick, DAOManager daoManager, Challenge challenge, Story story){
		
		request = FillerForMainMainStoriesAndChallenges.retrieveAndSetData(request,nick,daoManager);
		
		request.setAttribute("title", challenge.getTitle());
		
		FillDataMainChallengeLeader fillDataMainChallengeLeader = new FillDataMainChallengeLeader (challenge);
		
		request.setAttribute("titleStories", fillDataMainChallengeLeader.getTitles());
		request.setAttribute("participantStories", fillDataMainChallengeLeader.getParticipantStories());
		request.setAttribute("maxParticipantStories", fillDataMainChallengeLeader.getMaxParticipantStories());
		request.setAttribute("likes", fillDataMainChallengeLeader.getLikes());
		request.setAttribute("state", fillDataMainChallengeLeader.getState());
		
		// Recuperamos los datos de la historia 
		String title = story.getTitle();
		List <Fragment> fragments = daoManager.getFragmentsOrdered(story);
		List <String> fragmentsString = new ArrayList <String>();
		Iterator <Fragment> fragmentsIterator = fragments.iterator();
		while (fragmentsIterator.hasNext()){
			fragmentsString.add(fragmentsIterator.next().getFragment());
		}
							
		// Creamos la respuesta
		request.setAttribute ("titleStory", title);
		request.setAttribute ("Fragments", fragments);		
		
		return request;
		
	}

}
