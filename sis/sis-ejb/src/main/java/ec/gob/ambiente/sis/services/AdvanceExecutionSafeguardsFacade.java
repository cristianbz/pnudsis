package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;

@Stateless
public class AdvanceExecutionSafeguardsFacade extends AbstractFacade<AdvanceExecutionSafeguards, Integer> implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AdvanceExecutionSafeguardsFacade.class);

	public AdvanceExecutionSafeguardsFacade() {
		super(AdvanceExecutionSafeguards.class,Integer.class);
	}

}
