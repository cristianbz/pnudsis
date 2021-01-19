package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Sectors;

@Stateless
public class SectorsFacade extends AbstractFacade<Sectors, Integer> implements Serializable{


	private static final long serialVersionUID = 1L;

	public SectorsFacade() {
		super(Sectors.class,Integer.class);
	}

}
