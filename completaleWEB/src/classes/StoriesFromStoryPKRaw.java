package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import daoImpl.DAOManager;
import entities.Story;
import entities.StoryPK;

public class StoriesFromStoryPKRaw {
	
	public static List<StoryPK> getStoriesPKFromStoriesPKRaw (List<?> storiesRequestsPKRaw){
		
		List <StoryPK> storiesRequestsPK = new ArrayList <StoryPK>();
		
		Iterator<?> storiesRequestsPKRawIterator = storiesRequestsPKRaw.iterator();
		while (storiesRequestsPKRawIterator.hasNext()){
			Object currentObject = storiesRequestsPKRawIterator.next();
			Object [] objectArray = (Object[]) currentObject;
			String leader = (String) objectArray[0];
			Date openDate = (Date) objectArray[1];
			StoryPK storyPK = new StoryPK(leader, openDate);
			storiesRequestsPK.add(storyPK);
		}
		
		return storiesRequestsPK;
	}
	
	public static List <Story> getStoriesFromStoriesPK (List<StoryPK> storiesRequestsPK, DAOManager daoManager){
		
		List <Story> storiesRequests = new ArrayList <Story>();
		Iterator <StoryPK> storiesRequestsPKIterator = storiesRequestsPK.iterator();
		while (storiesRequestsPKIterator.hasNext()){
			StoryPK currentStoryPK = storiesRequestsPKIterator.next();				
			List <Story>storiesPK = daoManager.getActiveStoryFromStoryPK(currentStoryPK);
			if (storiesPK.size()>1) System.out.println("Error: Se han obtenido muchos storiesPK en StoriesFromStoryPKRaw");
			Story story=null;
			Iterator<Story>storiesPKIterator=storiesPK.iterator();
			while(storiesPKIterator.hasNext()==true){
				story=storiesPKIterator.next();
			}
			storiesRequests.add(story);
		}
		
		return storiesRequests;
	}
}
