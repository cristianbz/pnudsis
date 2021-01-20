package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
@Stateless
public class ProjectsSafeguardsFacade extends AbstractFacade<ProjectsSafeguardsFacade, Integer> implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ProjectsSafeguardsFacade.class);

	public ProjectsSafeguardsFacade() {
		super(ProjectsSafeguardsFacade.class,Integer.class);
	}

}
