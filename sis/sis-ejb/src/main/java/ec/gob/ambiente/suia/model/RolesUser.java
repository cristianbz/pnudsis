/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.suia.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="roles_users", schema="suia_iii")
@NamedQuery(name="RolesUser.findAll", query="SELECT r FROM RolesUser r")
public class RolesUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ROUS_ID_GENERATOR", initialValue = 1, sequenceName = "seq_rous_id", schema = "suia_iii", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROUS_ID_GENERATOR")
	@Column(name="rous_id")
	private Integer rousId;

	@Column(name="rous_status")
	private Boolean rousStatus;
	
	@Column(name="rous_description")
	private String rousDescription;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	
	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users users;

	public RolesUser() {
	}

	public Integer getRousId() {
		return this.rousId;
	}

	public void setRousId(Integer rousId) {
		this.rousId = rousId;
	}

	public Boolean getRousStatus() {
		return this.rousStatus;
	}

	public void setRousStatus(Boolean rousStatus) {
		this.rousStatus = rousStatus;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Users getUser() {
		return users;
	}

	public void setUser(Users user) {
		this.users = user;
	}

	public String getRousDescription() {
		return rousDescription;
	}

	public void setRousDescription(String rousDescription) {
		this.rousDescription = rousDescription;
	}
	
	
}