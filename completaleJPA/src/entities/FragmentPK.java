package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The primary key class for the fragment database table.
 * 
 */
@Embeddable
@Table(uniqueConstraints=
				@UniqueConstraint(columnNames = {"leader", "openDate", "id"}))
public class FragmentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=255)
	private String leader;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private java.util.Date openDate;

	@Column(nullable=false)
	private int id;

	public FragmentPK() {}
	
	public FragmentPK(String leader, Date openDate, int id) {
		this.leader = leader;
		this.openDate = openDate;
		this.id = id;
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
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FragmentPK)) {
			return false;
		}
		FragmentPK castOther = (FragmentPK)other;
		return 
			this.leader.equals(castOther.leader)
			&& this.openDate.equals(castOther.openDate)
			&& (this.id == castOther.id);
	}
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.leader.hashCode();
		hash = hash * prime + this.openDate.hashCode();
		hash = hash * prime + this.id;
		return hash;
	}
}