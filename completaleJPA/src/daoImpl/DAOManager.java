package daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import entities.Challenge;
import entities.Fragment;
import entities.Story;
import entities.StoryPK;
import entities.User;
import entities.Wrap;

public class DAOManager {
	
	private EntityManager em;
	private UserTransaction ut;	
	
	public DAOManager(EntityManager em, UserTransaction ut) {
		this.em=em;
		this.ut=ut;
	}

	public void insertUser (User user) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.persist(user);
		ut.commit();
	}
	
	public void removeUser (User user) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		user = em.find(User.class, user.getNick());
		em.remove(user);
		ut.commit();
	}
	
	public void mergeUserChangingNick (User newUser, String nickOldUser) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		User oldUser = em.find(User.class, nickOldUser);
		em.remove(oldUser);
		ut.commit();
		ut.begin();
		em.persist(newUser);
		ut.commit();
	}
	
	public void mergeUser (User newUser) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.merge(newUser);
		ut.commit();
	}
	
	public void insertStory (Story story) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.persist(story);
		ut.commit();
	}
	
	public void mergeStory (Story story) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.merge(story);
		ut.commit();
	}
	
	public void insertChallenge (Challenge challenge) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.persist(challenge);
		ut.commit();
	}
	
	public void mergeChallenge (Challenge challenge) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.merge(challenge);
		ut.commit();
	}
	
	public void insertWrap (Wrap wrap) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		ut.begin();
		em.persist(wrap);
		ut.commit();
	}
	 
	public List<User> getUser (String nick){
		Query q = em.createQuery("SELECT u FROM User u WHERE u.nick =:nick");
		q.setParameter("nick",nick);
		return q.getResultList();
	}
	
	public List<User> getUserByEmail (String email){
		Query q = em.createQuery("SELECT u FROM User u WHERE u.nick =:email");
		q.setParameter("email",email);
		return q.getResultList();
	}
	
	public List<User> matchUser (String nick, String password){
		Query q = em.createQuery("SELECT u FROM User u WHERE (u.nick =:nick) AND (u.password =:password)");
		q.setParameter("nick",nick);
		q.setParameter("password",password);
		return q.getResultList();
	}

	public List<User> getUserWithPartOfTheName (String nick){
		Query q = em.createQuery("SELECT u FROM User u WHERE u.nick LIKE ?1");
		q.setParameter(1, nick + "%");
		return q.getResultList();
	}
	
	public List<String> getFollowersWithPartOfTheName (String nick, String search){
		Query q = em.createNativeQuery("SELECT f.follower FROM Followup AS f WHERE (f.followed = ?) AND (f.follower LIKE ?)");
		q.setParameter(1, nick);
		q.setParameter(2, search + "%");
		return q.getResultList();
	}
	
	public List<String> getFollowings (String nick){
		Query q = em.createNativeQuery("SELECT followed FROM Followup WHERE follower = ?");
		q.setParameter(1, nick);
		return q.getResultList();
	}
	
	public List<String> getFollowingRequests (String nick){
		Query q = em.createNativeQuery("SELECT followed FROM receivedfollowingrequests WHERE follower = ?");
		q.setParameter(1, nick);
		return q.getResultList();
	}
		
	public List<?> getReceivedParticipationRequests (String nick){
		Query q = em.createNativeQuery("SELECT leader, opendate FROM userreceivedparticipationrequests WHERE nick=?", "ParticipationRequests");
		q.setParameter(1, nick);
		return q.getResultList();
	}
	
	public List<Story> getActiveStoryFromStoryPK (StoryPK storyPK){
		Query q = em.createQuery("SELECT s FROM Story s WHERE (s.id=?1) AND (s.state='1')");
		q.setParameter(1, storyPK);
		return q.getResultList();
	}
	
	public List<Fragment> getFragmentsOrdered (Story story){
		Query q = em.createQuery("SELECT f FROM Fragment f WHERE f.story=?1 ORDER BY f.id.id ASC");
		q.setParameter(1, story);
		return q.getResultList();
	}
	
	public List<Story> getActiveStoriesFromParticipant (User user){
		Query q = em.createQuery("SELECT s FROM Story s WHERE (s.usersParticipating=?1) AND (s.state='1')");
		q.setParameter(1, user);
		return q.getResultList();
	}
	
	public List<Story> getStoriesFromParticipant (User user){
		Query q = em.createQuery("SELECT s FROM Story s WHERE (s.usersParticipating=?1)");
		q.setParameter(1, user);
		return q.getResultList();
	}
	
	public List<Story> getStoriesFromTitle (String title){
		Query q = em.createQuery("SELECT s FROM Story s WHERE (s.title=?1)");
		q.setParameter(1, title);
		return q.getResultList();
	}
	
	public List<Challenge> getActiveChallengesFromManager (User user){
		Query q = em.createQuery("SELECT s FROM Challenge s WHERE (s.manager=?1) AND (s.state='1')");
		q.setParameter(1, user);
		return q.getResultList();
	}
}