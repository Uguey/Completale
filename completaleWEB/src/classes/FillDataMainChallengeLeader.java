package classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Challenge;
import entities.Story;
import entities.Wrap;

public class FillDataMainChallengeLeader {
	
	List <String> titles;
	List <Integer> participantStories;
	List <Byte> maxParticipantStories;
	List <Integer> likes;
	List <String> state;
	
	public FillDataMainChallengeLeader (Challenge newChallenge){
	
		List <Wrap> wrapStories = newChallenge.getStories();
		List <Story> stories = new ArrayList <Story>();
		Iterator <Wrap> wrapStoriesIterator = wrapStories.iterator();
		while(wrapStoriesIterator.hasNext()){
			Wrap currentWrap = wrapStoriesIterator.next();
			stories.add(currentWrap.getStory());
		}
		
		if (stories.isEmpty()==false) {
			
			titles = new ArrayList <String>();
			participantStories = new ArrayList <Integer>();
			maxParticipantStories = new ArrayList <Byte>();
			likes = new ArrayList <Integer>();
			state = new ArrayList <String>();
			
			Iterator <Story> storiesIterator = stories.iterator();
			while(storiesIterator.hasNext()){
				Story currentStory = storiesIterator.next();
				titles.add(currentStory.getTitle());
				participantStories.add(currentStory.getUsersParticipating().size());
				maxParticipantStories.add(currentStory.getMaxNumberOfParticipants());
				likes.add(currentStory.getLikes());
				if (currentStory.getState()==0) state.add("Cerrada");
				else if (currentStory.getState()==1) state.add("Abierta");
				else System.out.println("Error: se han obtenido estados no contemplados en CreateChallenge");
			}
		}		
	}

	public List<String> getTitles() {
		return titles;
	}
	public List<Integer> getParticipantStories() {
		return participantStories;
	}
	public List<Byte> getMaxParticipantStories() {
		return maxParticipantStories;
	}
	public List<Integer> getLikes() {
		return likes;
	}
	public List<String> getState() {
		return state;
	}
}
