package ec.gob.ambiente.suia.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.suia.model.Menu;
import ec.gob.ambiente.suia.model.MenuVO;
import ec.gob.ambiente.suia.model.RolesUser;





@Stateless
public class MenuFacade extends  AbstractFacade <Menu, Integer> implements Serializable{

	private static final long serialVersionUID = 1551132779840660637L;

	public MenuFacade() {
		super(Menu.class, Integer.class);		
	}
	
	/**
	 * Buscar por MNEMONIC
	 * @param mnemonic
	 * @return
	 */
	public Menu findByMnemonic(String mnemonic)
	{
		TypedQuery<Menu> query = super.getEntityManager().createQuery("select o from Menu o where o.menuStatus = true and o.menuMnemonic = :mnemonic", Menu.class);		
		query.setParameter("mnemonic", mnemonic);
		return (Menu)query.getSingleResult();
		
	}
	
	/**
	 * Buscar por roles
	 * @param roles
	 * @param menuMnemonic
	 * @return
	 */
	private List<Menu> findByRoles(List<Integer> roles, String menuMnemonic)
	{
		List<Menu> menus = new ArrayList<Menu>();
		if(roles.size() > 0)
		{
			Query query = super.getEntityManager().createQuery("select distinct mr.menu.menuId from MenuRole mr where mr.role.roleId in :roles and mr.menu.menuMnemonic != :menuMnemonic and mr.menu.menuSampleMenu = true");
			query.setParameter("roles", roles);
			query.setParameter("menuMnemonic", menuMnemonic);
			
			List<Integer> menusIds = (List<Integer>) query.getResultList();
			
			TypedQuery<Menu> queryMenu = super.getEntityManager().createQuery("select m from Menu m where m.menuId in :menusIds and m.menuMnemonic != :menuMnemonic and m.menuSampleMenu = true order by m.menuOrder", Menu.class);
			
			queryMenu.setParameter("menuMnemonic", menuMnemonic);
			queryMenu.setParameter("menusIds", menusIds);			
			
			menus = (List<Menu>) queryMenu.getResultList();
			
			return menus;
		}
		else
		{
			return menus;
		}
	}
		
	/**
	 * Buscar por roles
	 * @param rolesUsers
	 * @param menuMnemonic
	 * @return MenuVO
	 */
	public List<MenuVO> getMenusByRoles(List<RolesUser> rolesUsers, String menuMnemonic)
	{
		try {
			
		
			List<MenuVO> menus = new ArrayList<MenuVO>();
			Integer userId = 0;
			Integer parentMenu = 0;
			List<Integer> listaRoles = new ArrayList<Integer>();
			for (RolesUser rolUser : rolesUsers) {
				userId = rolUser.getUser().getUserId();
				listaRoles.add(rolUser.getRole().getRoleId());
			}
	    
			List<Menu> menusList =findByRoles(listaRoles, menuMnemonic);
			for (Menu menu : menusList) {
				if(menu != null && menu.getMenu() != null){
					parentMenu = (menu.getMenu().getMenuId() != null || menu.getMenu().getMenuId() != 0) ? menu.getMenu().getMenuId() : 0;
	    	
					menus.add(new MenuVO(menu.getMenuId(), menu.getMenuName(), menu.getMenuAction(), menu.getMenuUrl(), parentMenu, userId, menu.getMenuEndNode(), menu.getMenuOrder(), menu.getMenuIcon()));
				}
			}			
	    
			return menus;
	    
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

}