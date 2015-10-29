package classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entities.Story;
import entities.User;

public class LikesAndIfIFollowAllFollower {
	
	public static Map <User, Integer> getLikes (List <User> users){
		
		Map <User, Integer> userLikes = new HashMap <User, Integer>();
		List <Story> storiesEachUser;
		Iterator <User> userIterator = users.iterator();
		while(userIterator.hasNext()){
			User currentUser = userIterator.next();
			storiesEachUser = currentUser.getStoriesIParticipated();
			Iterator <Story> storiesEachUserIterator = storiesEachUser.iterator();
			int likesEachuser = 0;
			while(storiesEachUserIterator.hasNext()){
				likesEachuser = storiesEachUserIterator.next().getLikes();
			}
			userLikes.put(currentUser, likesEachuser);
		}
		
		return userLikes;
	}
	
	public static Map <User, Boolean> getIfIFollowFollower (List <User> followers, List <String> followings) {
	
		Map <User, Boolean> userFollowerTrue = new HashMap <User, Boolean>();
		
		Iterator <User> followerIterator = followers.iterator();
		
		boolean iFollowFollower = false;
		while (followerIterator.hasNext()){
			User currentFollower = followerIterator.next();
			Iterator <String> followingsIterator = followings.iterator();
			while (followingsIterator.hasNext()){
				String currentFollowing = followingsIterator.next();
				if (currentFollower.getNick().equals(currentFollowing)) {
					iFollowFollower = true;					
				}
			}
			if (iFollowFollower==true) userFollowerTrue.put(currentFollower, true);
			else userFollowerTrue.put(currentFollower, false);
			iFollowFollower = false;
		}
		return userFollowerTrue;
	}
}
