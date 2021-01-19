package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;

@Stateless
public class AdvanceExecutionSafeguardsFacade extends AbstractFacade<AdvanceExecutionSafeguards, Integer> implements Serializable{

	
	private static final long serialVersionUID = 1L;

	public AdvanceExecutionSafeguardsFacade() {
		super(AdvanceExecutionSafeguards.class,Integer.class);
	}

}
