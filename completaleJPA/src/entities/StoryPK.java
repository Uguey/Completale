package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The primary key class for the story database table.
 */
@SqlResultSetMapping(name="ParticipationRequests", 
entities={@EntityResult
			(entityClass=StoryPK.class, 
				fields={@FieldResult(name="leader", column="leader"),
						@FieldResult(name="openDate", column="opendate")
				}
			)
}
)
@Embeddable
@Table(uniqueConstraints=
				@UniqueConstraint(columnNames = {"leader", "openDate"}))
public class StoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=255)
	private String leader;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private java.util.Date openDate;

	public StoryPK() {}
	
	public StoryPK(String leader, Date openDate) {
		this.leader = leader;
		this.openDate = openDate;
	}
	
	public String getLeader() {
		return this.leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	
	public java.util.Date getOpenDate() {
		return this.openDate;
	}
	public void setOpenDate(java.util.Date openDate) {
		this.openDate = openDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StoryPK)) {
			return false;
		}
		StoryPK castOther = (StoryPK)other;
		return 
			this.leader.equals(castOther.leader)
			&& this.openDate.equals(castOther.openDate);
	}
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.leader.hashCode();
		hash = hash * prime + this.openDate.hashCode();
		return hash;
	}
}