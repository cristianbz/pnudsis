package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceSectors;

@Stateless
public class AdvanceSectorsFacade extends AbstractFacade<AdvanceSectors,Integer> implements Serializable{

	
	private static final long serialVersionUID = 1L;

	public AdvanceSectorsFacade() {
		super(AdvanceSectors.class,Integer.class);
	}

}
