package ec.gob.ambiente.sis.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceSectors;

@Stateless
@LocalBean
public class AdvanceSectorsFacade extends AbstractFacade<AdvanceSectors,Integer> {

	
	

	public AdvanceSectorsFacade() {
		super(AdvanceSectors.class,Integer.class);
	}

}
