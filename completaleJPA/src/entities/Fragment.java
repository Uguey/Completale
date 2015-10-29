package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the fragment database table.
 */
@Entity
@Table(name="fragment")
@NamedQuery(name="Fragment.findAll", query="SELECT f FROM Fragment f")
public class Fragment implements Serializable {
	private static final long serialVersionUID = 1L;

	/* ATTRIBUTES */
	@EmbeddedId
	@Column(unique=true, nullable=false)
	private FragmentPK id;

	/* STORY */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="Leader", referencedColumnName="Leader", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="OpenDate", referencedColumnName="OpenDate", nullable=false, insertable=false, updatable=false)
		})
	private Story story;

	/* WRITER */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Nick")
	private User writer;
	
	/* TEXT */
	@Lob
	@Column(nullable=false)
	private String fragment;

	public Fragment() {}
	
	public Fragment(FragmentPK id, Story story, User writer, String fragment) {
		this.id = id;
		this.story = story;
		this.writer = writer;
		this.fragment = fragment;
	}

	public FragmentPK getId() {
		return this.id;
	}
	public void setId(FragmentPK id) {
		this.id = id;
	}

	public String getFragment() {
		return this.fragment;
	}
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public Story getStory() {
		return this.story;
	}
	public void setStory(Story story) {
		this.story = story;
	}

	public User getWriter() {
		return this.writer;
	}
	public void setWriter(User writer) {
		this.writer = writer;
	}
}