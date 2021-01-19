package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
@Stateless
public class ProjectsSafeguardsFacade extends AbstractFacade<ProjectsSafeguardsFacade, Integer> implements Serializable{

	private static final long serialVersionUID = 1L;

	public ProjectsSafeguardsFacade() {
		super(ProjectsSafeguardsFacade.class,Integer.class);
	}

}
