package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import daoImpl.DAOManager;
import entities.Story;
import entities.StoryPK;
import entities.User;

public class FillStory {
	
	public static Story createStory (String leader, String title, int maxNumberOfParticipants, ArrayList <String> participants, DAOManager daoManager){

		StoryPK newStoryPK = new StoryPK (leader, new Date());
		
		List<User> users = daoManager.getUser(leader);
		if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
		User user=null;
		Iterator<User>usersIterator=users.iterator();
		while(usersIterator.hasNext()==true){
			user=usersIterator.next();
		}
		
		List<User> usersParticipating = new ArrayList <User>();
		usersParticipating.add(user);
		List<User> participationRequest = new ArrayList <User>();
		for (int i=0; i<participants.size();i++){
			List<User> currentParticipantsRequest = daoManager.getUser(participants.get(i));
			if(users.size()>1) System.out.println("Error: Se han obtenido muchos usuarios en DataCharger");
			if(currentParticipantsRequest.isEmpty()) return null;
			else {
				User currentParticipantRequest=null;
				Iterator<User>currentParticipantsRequestIterator=currentParticipantsRequest.iterator();
				while(currentParticipantsRequestIterator.hasNext()==true){
					currentParticipantRequest=currentParticipantsRequestIterator.next();
				}
				participationRequest.add(currentParticipantRequest);
			}
		}
		
		Story newStory = new Story (newStoryPK, title, (byte) maxNumberOfParticipants, 0, (byte)1,
				leader, usersParticipating, participationRequest, 
				null, null, null, null, null);
		
		return newStory;
	}

}
