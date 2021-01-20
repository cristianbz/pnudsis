package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.TableResponses;

@Stateless
public class TableResponsesFacade extends AbstractFacade<TableResponses, Integer> implements Serializable {


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TableResponsesFacade.class);

	public TableResponsesFacade() {
		super(TableResponses.class,Integer.class);
	}

}
