package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import static javax.persistence.FetchType.EAGER;

/**
 * The persistent class for the user database table.
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/* ATTRIBUTES */
	@Id
	@Column(unique=true, nullable=false, length=255)
	private String nick;
	
	@Column(unique=true, nullable=false, length=255)
	private String email;
	
	@Column(nullable=false, length=255)
	private String password;
	
	/* STORIES */
	@ManyToMany(mappedBy="usersParticipating")
	private List<Story> storiesIParticipated;

	@ManyToMany(mappedBy="usersSentParticipationRequests", fetch = EAGER)
	private List<Story> receivedParticipationRequests;

	@ManyToMany
	@JoinTable(
		name="storyReceivedParticipationRequests"
		, joinColumns={
			@JoinColumn(name="Nick", referencedColumnName="nick", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="Leader", referencedColumnName="Leader", nullable=false),
			@JoinColumn(name="OpenDate", referencedColumnName="OpenDate", nullable=false)
			}
		)
	private List<Story> storiesSentParticipationRequests;

	/* FOLLOWING */ 
	@ManyToMany
	@JoinTable(
		name="followup"
		, joinColumns={
			@JoinColumn(name="Followed", referencedColumnName="nick", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="Follower", referencedColumnName="nick", nullable=false)
			}
		)
	private List<User> followings;
	
	@ManyToMany(mappedBy="followings")
	private List<User> followers;

	@ManyToMany
	@JoinTable(
		name="receivedFollowingRequests"
		, joinColumns={
			@JoinColumn(name="Followed", referencedColumnName="nick", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="Follower", referencedColumnName="nick", nullable=false)
			}
		)
	private List<User> followingRequests;

	@ManyToMany(mappedBy="followingRequests")
	private List<User> followersRequests;

	/* CHALLENGES */ 
	@OneToMany(mappedBy="manager")
	private List<Challenge> challenges;

	/* FRAGMENTS */ 
	@OneToMany(mappedBy="writer")
	private List<Fragment> fragments;

	public User() {}
	
	public User(String nick, String email, String password, List<Story> storiesIParticipated,
			List<Story> receivedParticipationRequests, List<Story> storiesSentParticipationRequests,
			List<User> followings, List<User> followers, List<User> followingRequests, List<User> followersRequests,
			List<Challenge> challenges, List<Fragment> fragments) {
		this.nick = nick;
		this.email = email;
		this.password = password;
		this.storiesIParticipated = storiesIParticipated;
		this.receivedParticipationRequests = receivedParticipationRequests;
		this.storiesSentParticipationRequests = storiesSentParticipationRequests;
		this.followings = followings;
		this.followers = followers;
		this.followingRequests = followingRequests;
		this.followersRequests = followersRequests;
		this.challenges = challenges;
		this.fragments = fragments;
	}

	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Story> getStoriesIParticipated() {
		return storiesIParticipated;
	}
	public void setStoriesIParticipated(List<Story> storiesIParticipated) {
		this.storiesIParticipated = storiesIParticipated;
	}
	
	public List<Story> getReceivedParticipationRequests() {
		return receivedParticipationRequests;
	}
	public void setReceivedParticipationRequests(List<Story> receivedParticipationRequests) {
		this.receivedParticipationRequests = receivedParticipationRequests;
	}

	public List<Story> getStoriesSentParticipationRequests() {
		return storiesSentParticipationRequests;
	}
	public void setStoriesSentParticipationRequests(List<Story> storiesSentParticipationRequests) {
		this.storiesSentParticipationRequests = storiesSentParticipationRequests;
	}

	public List<User> getFollowings() {
		return followings;
	}
	public void setFollowings(List<User> followings) {
		this.followings = followings;
	}

	public List<User> getFollowers() {
		return followers;
	}
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public List<User> getFollowingRequests() {
		return followingRequests;
	}
	public void setFollowingRequests(List<User> followingRequests) {
		this.followingRequests = followingRequests;
	}

	public List<User> getFollowersRequests() {
		return followersRequests;
	}
	public void setFollowersRequests(List<User> followersRequests) {
		this.followersRequests = followersRequests;
	}

	public List<Challenge> getChallenges() {
		return this.challenges;
	}
	public void setChallenges(List<Challenge> challenges) {
		this.challenges = challenges;
	}
	public Challenge addChallenge(Challenge challenge) {
		getChallenges().add(challenge);
		challenge.setManager(this);
		return challenge;
	}
	public Challenge removeChallenge(Challenge challenge) {
		getChallenges().remove(challenge);
		challenge.setManager(null);
		return challenge;
	}

	public List<Fragment> getFragments() {
		return this.fragments;
	}
	public void setFragments(List<Fragment> fragments) {
		this.fragments = fragments;
	}
	public Fragment addFragment(Fragment fragment) {
		getFragments().add(fragment);
		fragment.setWriter(this);
		return fragment;
	}
	public Fragment removeFragment(Fragment fragment) {
		getFragments().remove(fragment);
		fragment.setWriter(null);
		return fragment;
	}
}