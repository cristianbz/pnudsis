package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;

@Stateless
public class ExecutiveSummariesFacade extends AbstractFacade<ExecutiveSummaries, Integer> implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ExecutiveSummariesFacade.class);

	public ExecutiveSummariesFacade() {
		super(ExecutiveSummaries.class,Integer.class);
	}

}
