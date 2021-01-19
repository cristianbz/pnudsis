package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.TableResponses;

@Stateless
public class TableResponsesFacade extends AbstractFacade<TableResponses, Integer> implements Serializable {


	private static final long serialVersionUID = 1L;

	public TableResponsesFacade() {
		super(TableResponses.class,Integer.class);
	}

}
