package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;

@Stateless
public class ExecutiveSummariesFacade extends AbstractFacade<ExecutiveSummaries, Integer> implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public ExecutiveSummariesFacade() {
		super(ExecutiveSummaries.class,Integer.class);
	}

}
