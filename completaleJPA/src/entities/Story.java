package entities;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the story database table.
 */
@Entity
@Table(name="story")
@NamedQuery(name="Story.findAll", query="SELECT s FROM Story s")
public class Story implements Serializable {
	private static final long serialVersionUID = 1L;

	/* ATTRIBUTES */
	@EmbeddedId
	@Column(unique=true, nullable=false)
	private StoryPK id;
	
	@Column(nullable=false, length=255)
	private String title;
	
	@Column(nullable=false)
	private byte maxNumberOfParticipants;
	
	@Column(nullable=false)
	private int likes;
	
	@Column(nullable=false)
	private byte state;

	@Column(length=255)
	private String actualParticipant;

	/* USERS */
	@ManyToMany
	@JoinTable(
		name="participate"
		, joinColumns={
			@JoinColumn(name="Leader", referencedColumnName="Leader", nullable=false),
			@JoinColumn(name="OpenDate", referencedColumnName="OpenDate", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="Nick", referencedColumnName="nick", nullable=false)
			}
		)
	private List<User> usersParticipating;

	@ManyToMany(fetch = EAGER)
	@JoinTable(
		name="userReceivedParticipationRequests"
		, joinColumns={
			@JoinColumn(name="Leader", referencedColumnName="Leader", nullable=false),
			@JoinColumn(name="OpenDate", referencedColumnName="OpenDate", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="Nick", referencedColumnName="nick", nullable=false)
			}
		)
	private List<User> usersSentParticipationRequests;

	@ManyToMany(mappedBy="storiesSentParticipationRequests")
	private List<User> receivedParticipationRequests;

	/* CHALLENGES PARTICIPATION REQUESTS */
	@ManyToMany
	@JoinTable(
		name="challengeReceivedParticipationRequests"
		, joinColumns={
			@JoinColumn(name="LeaderStory", referencedColumnName="Leader", nullable=false),
			@JoinColumn(name="OpenDateStory", referencedColumnName="OpenDate", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="LeaderChallenge", referencedColumnName="Leader", nullable=false),
			@JoinColumn(name="OpenDateChallenge", referencedColumnName="OpenDate", nullable=false)
			}
		)
	private List<Challenge> challengeSentParticipationRequests;

	/* WINNER STORY */
	@OneToOne(mappedBy="story", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Winnerstory winnerStory;

	/* CHALLENGE */ 
	@OneToOne(mappedBy="story", fetch=FetchType.LAZY, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private Wrap challenge;
	
	/* FRAGMENTS */
	@OneToMany(mappedBy="story", fetch=FetchType.LAZY)
	private List <Fragment> fragments;

	public Story() {}
	
	public Story(StoryPK id, String title, byte maxNumberOfParticipants, int likes, byte state,
			String actualParticipant, List<User> usersParticipating, List<User> usersSentParticipationRequests,
			List<User> receivedParticipationRequests, List<Challenge> challengeSentParticipationRequests,
			Winnerstory winnerStory, Wrap challenge, List<Fragment> fragments) {
		this.id = id;
		this.title = title;
		this.maxNumberOfParticipants = maxNumberOfParticipants;
		this.likes = likes;
		this.state = state;
		this.actualParticipant = actualParticipant;
		this.usersParticipating = usersParticipating;
		this.usersSentParticipationRequests = usersSentParticipationRequests;
		this.receivedParticipationRequests = receivedParticipationRequests;
		this.challengeSentParticipationRequests = challengeSentParticipationRequests;
		this.winnerStory = winnerStory;
		this.challenge = challenge;
		this.fragments = fragments;
	}

	public StoryPK getId() {
		return id;
	}
	public void setId(StoryPK id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public byte getMaxNumberOfParticipants() {
		return maxNumberOfParticipants;
	}
	public void setMaxNumberOfParticipants(byte maxNumberOfParticipants) {
		this.maxNumberOfParticipants = maxNumberOfParticipants;
	}

	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}

	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}

	public String getActualParticipant() {
		return actualParticipant;
	}
	public void setActualParticipant(String actualParticipant) {
		this.actualParticipant = actualParticipant;
	}

	public List<User> getUsersParticipating() {
		return usersParticipating;
	}
	public void setUsersParticipating(List<User> usersParticipating) {
		this.usersParticipating = usersParticipating;
	}

	public List<User> getUsersSentParticipationRequests() {
		return usersSentParticipationRequests;
	}
	public void setUsersSentParticipationRequests(List<User> usersSentParticipationRequests) {
		this.usersSentParticipationRequests = usersSentParticipationRequests;
	}

	public List<User> getReceivedParticipationRequests() {
		return receivedParticipationRequests;
	}
	public void setReceivedParticipationRequests(List<User> receivedParticipationRequests) {
		this.receivedParticipationRequests = receivedParticipationRequests;
	}

	public List<Challenge> getChallengeSentParticipationRequests() {
		return challengeSentParticipationRequests;
	}
	public void setChallengeSentParticipationRequests(List<Challenge> challengeSentParticipationRequests) {
		this.challengeSentParticipationRequests = challengeSentParticipationRequests;
	}

	public Winnerstory getWinnerStory() {
		return winnerStory;
	}
	public void setWinnerStory(Winnerstory winnerStory) {
		this.winnerStory = winnerStory;
	}

	public Wrap getChallenge() {
		return challenge;
	}
	public void setChallenge(Wrap challenge) {
		this.challenge = challenge;
	}

	public List<Fragment> getFragments() {
		return fragments;
	}
	public void setFragments(List<Fragment> fragments) {
		this.fragments = fragments;
	}
}