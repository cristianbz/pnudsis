/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.suia.model;
import java.io.Serializable;

import javax.persistence.*;

import java.util.List;
@Entity
@Table(name="menu", schema="suia_iii")
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name="menu_id")
	private Integer menuId;

	@Column(name="menu_action")
	private String menuAction;

	@Column(name="menu_end_node")
	private Boolean menuEndNode;

	@Column(name="menu_icon")
	private String menuIcon;

	@Column(name="menu_mnemonic")
	private String menuMnemonic;

	@Column(name="menu_name")
	private String menuName;

	@Column(name="menu_order")
	private Integer menuOrder;

	@Column(name="menu_sample_menu")
	private Boolean menuSampleMenu;

	@Column(name="menu_status")
	private Boolean menuStatus;

	@Column(name="menu_url")
	private String menuUrl;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="menu_father_id")
	private Menu menu;

	//bi-directional many-to-one association to Menu
	@OneToMany(mappedBy="menu")
	private List<Menu> menus;

	//bi-directional many-to-one association to MenuRole
	@OneToMany(mappedBy="menu")
	private List<MenuRole> menuRoles;

	public Menu() {
	}

	public Integer getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuAction() {
		return this.menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	public Boolean getMenuEndNode() {
		return this.menuEndNode;
	}

	public void setMenuEndNode(Boolean menuEndNode) {
		this.menuEndNode = menuEndNode;
	}

	public String getMenuIcon() {
		return this.menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getMenuMnemonic() {
		return this.menuMnemonic;
	}

	public void setMenuMnemonic(String menuMnemonic) {
		this.menuMnemonic = menuMnemonic;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getMenuOrder() {
		return this.menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public Boolean getMenuSampleMenu() {
		return this.menuSampleMenu;
	}

	public void setMenuSampleMenu(Boolean menuSampleMenu) {
		this.menuSampleMenu = menuSampleMenu;
	}

	public Boolean getMenuStatus() {
		return this.menuStatus;
	}

	public void setMenuStatus(Boolean menuStatus) {
		this.menuStatus = menuStatus;
	}

	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Menu addMenus(Menu menus) {
		getMenus().add(menus);
		menus.setMenu(this);

		return menus;
	}

	public Menu removeMenus(Menu menus) {
		getMenus().remove(menus);
		menus.setMenu(null);

		return menus;
	}

	public List<MenuRole> getMenuRoles() {
		return this.menuRoles;
	}

	public void setMenuRoles(List<MenuRole> menuRoles) {
		this.menuRoles = menuRoles;
	}

	public MenuRole addMenuRole(MenuRole menuRole) {
		getMenuRoles().add(menuRole);
		menuRole.setMenu(this);

		return menuRole;
	}

	public MenuRole removeMenuRole(MenuRole menuRole) {
		getMenuRoles().remove(menuRole);
		menuRole.setMenu(null);

		return menuRole;
	}

}