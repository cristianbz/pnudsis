package ec.gob.ambiente.sis.services;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;

@Stateless
public class ExecutiveSummariesFacade extends AbstractFacade<ExecutiveSummaries, Integer>  {

	
	

	public ExecutiveSummariesFacade() {
		super(ExecutiveSummaries.class,Integer.class);
	}

}
