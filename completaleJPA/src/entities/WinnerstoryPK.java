package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The primary key class for the winnerstories database table.
 * 
 */
@Embeddable
@Table(uniqueConstraints=
			@UniqueConstraint(columnNames = {"leaderStory", "openDateStory"}))
public class WinnerstoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=255)
	private String leaderStory;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private java.util.Date openDateStory;

	public WinnerstoryPK() {}
	
	public WinnerstoryPK(String leaderStory, Date openDateStory) {
		this.leaderStory = leaderStory;
		this.openDateStory = openDateStory;
	}

	public String getLeaderStory() {
		return this.leaderStory;
	}
	public void setLeaderStory(String leaderStory) {
		this.leaderStory = leaderStory;
	}
	public java.util.Date getOpenDateStory() {
		return this.openDateStory;
	}
	public void setOpenDateStory(java.util.Date openDateStory) {
		this.openDateStory = openDateStory;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WinnerstoryPK)) {
			return false;
		}
		WinnerstoryPK castOther = (WinnerstoryPK)other;
		return 
			this.leaderStory.equals(castOther.leaderStory)
			&& this.openDateStory.equals(castOther.openDateStory);
	}
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.leaderStory.hashCode();
		hash = hash * prime + this.openDateStory.hashCode();
		return hash;
	}
}