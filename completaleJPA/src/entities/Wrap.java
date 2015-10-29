package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the wrap database table.
 */
@Entity
@Table(name="wrap")
@NamedQuery(name="Wrap.findAll", query="SELECT w FROM Wrap w")
public class Wrap implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Column(insertable=false, updatable=false)
	private WrapPK id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="LeaderChallenge", referencedColumnName="Leader"),
		@JoinColumn(name="OpenDateChallenge", referencedColumnName="OpenDate")
		})
	private Challenge challenge;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="LeaderStory", referencedColumnName="Leader", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="OpenDateStory", referencedColumnName="OpenDate", nullable=false, insertable=false, updatable=false)
		})
	private Story story;

	public Wrap() {}
	
	public Wrap(WrapPK id, Challenge challenge, Story story) {
		this.id = id;
		this.challenge = challenge;
		this.story = story;
	}

	public WrapPK getId() {
		return this.id;
	}

	public void setId(WrapPK id) {
		this.id = id;
	}

	public Challenge getChallenge() {
		return this.challenge;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	public Story getStory() {
		return this.story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

}