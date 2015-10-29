package classes;

import java.util.Iterator;
import java.util.List;

import daoImpl.DAOManager;
import entities.Story;
import entities.User;

public class FillDataMain {
	
	private String nick;
	private int likes;
	private int followers;
	private int followingRequests;
	private int storyParticipationRequests;
	
	
	public FillDataMain (User user, DAOManager daoManager){
		
		List<User>followings=user.getFollowings();
		List<User>followingRequests=user.getFollowingRequests();
		List<?>storyParticipationRequestsPKRaw=daoManager.getReceivedParticipationRequests(user.getNick());
		
		List<Story>stories=daoManager.getStoriesFromParticipant (user);
		Iterator<Story>storiesIterator=stories.iterator();
		int likes = 0;
		while(storiesIterator.hasNext()){
			likes+=storiesIterator.next().getLikes();
		}
		
		this.nick=user.getNick();
		this.likes=likes;
		this.followers=followings.size();
		this.followingRequests=followingRequests.size();
		this.storyParticipationRequests=storyParticipationRequestsPKRaw.size();		
	}

	public String getNick() {
		return nick;
	}
	public int getLikes() {
		return likes;
	}
	public int getFollowers() {
		return followers;
	}
	public int getFollowingRequests() {
		return followingRequests;
	}
	public int getStoryParticipationRequests() {
		return storyParticipationRequests;
	}
}
