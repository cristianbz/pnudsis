package ec.gob.ambiente.suia.service;
import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.suia.model.MenuRole;





@Stateless
public class MenuRoleFacade extends AbstractFacade<MenuRole, Integer> implements Serializable {

	private static final long serialVersionUID = -1397835309701774809L;

	public MenuRoleFacade() {
		super(MenuRole.class, Integer.class);		
	}	
}