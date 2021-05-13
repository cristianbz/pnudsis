/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.suia.model;
import java.io.Serializable;

import javax.persistence.*;

import java.util.List;
@Entity
@Table(name="roles", schema="suia_iii")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ROLE_ID_GENERATOR", initialValue = 1, sequenceName = "seq_role_id", schema = "suia_iii", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID_GENERATOR")
	@Column(name="role_id")
	private Integer roleId;

	@Column(name="role_description")
	private String roleDescription;

	@Column(name="role_name")
	private String roleName;

	@Column(name="role_status")
	private Boolean roleStatus;

	@Column(name="role_unique")
	private Boolean roleUnique;

	//bi-directional many-to-one association to MenuRole
	@OneToMany(mappedBy="role")
	private List<MenuRole> menuRoles;

	//bi-directional many-to-one association to RolesUser
	@OneToMany(mappedBy="role")
	private List<RolesUser> rolesUsers;

	public Role() {
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleDescription() {
		return this.roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getRoleStatus() {
		return this.roleStatus;
	}

	public void setRoleStatus(Boolean roleStatus) {
		this.roleStatus = roleStatus;
	}

	public Boolean getRoleUnique() {
		return this.roleUnique;
	}

	public void setRoleUnique(Boolean roleUnique) {
		this.roleUnique = roleUnique;
	}

	public List<MenuRole> getMenuRoles() {
		return this.menuRoles;
	}

	public void setMenuRoles(List<MenuRole> menuRoles) {
		this.menuRoles = menuRoles;
	}

	public MenuRole addMenuRole(MenuRole menuRole) {
		getMenuRoles().add(menuRole);
		menuRole.setRole(this);

		return menuRole;
	}

	public MenuRole removeMenuRole(MenuRole menuRole) {
		getMenuRoles().remove(menuRole);
		menuRole.setRole(null);

		return menuRole;
	}

	public List<RolesUser> getRolesUsers() {
		return this.rolesUsers;
	}

	public void setRolesUsers(List<RolesUser> rolesUsers) {
		this.rolesUsers = rolesUsers;
	}

	public RolesUser addRolesUser(RolesUser rolesUser) {
		getRolesUsers().add(rolesUser);
		rolesUser.setRole(this);

		return rolesUser;
	}

	public RolesUser removeRolesUser(RolesUser rolesUser) {
		getRolesUsers().remove(rolesUser);
		rolesUser.setRole(null);

		return rolesUser;
	}

}