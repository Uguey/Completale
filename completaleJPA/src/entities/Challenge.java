package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;

/**
 * The persistent class for the challenge database table.
 */
@Entity
@Table(name="challenge")
@NamedQuery(name="Challenge.findAll", query="SELECT c FROM Challenge c")
public class Challenge implements Serializable {
	private static final long serialVersionUID = 1L;

	/* ATTRIBUTES */
	@EmbeddedId
	@Column(unique=true, nullable=false)
	private ChallengePK id;
	
	@Column(nullable=false, length=255, unique=true)
	private String title;

	@Lob
	private String description;

	@Column(nullable=false)
	private int maxNumberOfStories;

	private BigInteger maxNumberOfWordsPerStory;

	@Column(nullable=false)
	private byte state;

	@Temporal(TemporalType.TIMESTAMP)
	private Date winnersElectionDate;

	@Lob
	private String winnersMessage;

	/* LEADER */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Nick")
	private User manager;
	
	/* STORIESREQUEST */
	@ManyToMany(mappedBy="challengeSentParticipationRequests")
	private List<Story> receivedParticipationRequests;

	/* WINNERSTORIES */
	@OneToMany(mappedBy="challenge", cascade=CascadeType.ALL)
	private List<Winnerstory> winnerStories;

	/* STORIES */
	@OneToMany(mappedBy="challenge", 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private List<Wrap> stories;

	public Challenge() {}
	
	public Challenge(ChallengePK id, String title, String description, int maxNumberOfStories,
			BigInteger maxNumberOfWordsPerStory, byte state, Date winnersElectionDate, String winnersMessage,
			User manager, List<Story> receivedParticipationRequests, List<Winnerstory> winnerStories,
			List<Wrap> stories) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.maxNumberOfStories = maxNumberOfStories;
		this.maxNumberOfWordsPerStory = maxNumberOfWordsPerStory;
		this.state = state;
		this.winnersElectionDate = winnersElectionDate;
		this.winnersMessage = winnersMessage;
		this.manager = manager;
		this.receivedParticipationRequests = receivedParticipationRequests;
		this.winnerStories = winnerStories;
		this.stories = stories;
	}

	public ChallengePK getId() {
		return id;
	}
	public void setId(ChallengePK id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxNumberOfStories() {
		return maxNumberOfStories;
	}
	public void setMaxNumberOfStories(int maxNumberOfStories) {
		this.maxNumberOfStories = maxNumberOfStories;
	}

	public BigInteger getMaxNumberOfWordsPerStory() {
		return maxNumberOfWordsPerStory;
	}
	public void setMaxNumberOfWordsPerStory(BigInteger maxNumberOfWordsPerStory) {
		this.maxNumberOfWordsPerStory = maxNumberOfWordsPerStory;
	}

	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}

	public Date getWinnersElectionDate() {
		return winnersElectionDate;
	}
	public void setWinnersElectionDate(Date winnersElectionDate) {
		this.winnersElectionDate = winnersElectionDate;
	}

	public String getWinnersMessage() {
		return winnersMessage;
	}
	public void setWinnersMessage(String winnersMessage) {
		this.winnersMessage = winnersMessage;
	}

	public User getManager() {
		return manager;
	}
	public void setManager(User manager) {
		this.manager = manager;
	}

	public List<Story> getReceivedParticipationRequests() {
		return receivedParticipationRequests;
	}
	public void setReceivedParticipationRequests(List<Story> receivedParticipationRequests) {
		this.receivedParticipationRequests = receivedParticipationRequests;
	}

	public List<Winnerstory> getWinnerStories() {
		return this.winnerStories;
	}
	public void setWinnerStories(List<Winnerstory> winnerStories) {
		this.winnerStories = winnerStories;
	}
	public Winnerstory addWinnerStory(Winnerstory winnerStory) {
		getWinnerStories().add(winnerStory);
		winnerStory.setChallenge(this);
		return winnerStory;
	}
	public Winnerstory removeWinnerStory(Winnerstory winnerStory) {
		getWinnerStories().remove(winnerStory);
		winnerStory.setChallenge(null);
		return winnerStory;
	}

	public List<Wrap> getStories() {
		return this.stories;
	}
	public void setStories(List<Wrap> stories) {
		this.stories = stories;
	}
	public Wrap addStory(Wrap story) {
		getStories().add(story);
		story.setChallenge(this);
		return story;
	}
	public Wrap removeStory(Wrap story) {
		getStories().remove(story);
		story.setChallenge(null);
		return story;
	}
}