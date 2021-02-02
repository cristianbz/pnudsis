package ec.gob.ambiente.sigma.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
public class ProjectsSafeguardsFacade extends AbstractFacade<ProjectsSafeguards, Integer> implements Serializable {
	

	private static final long serialVersionUID = 1L;

	public ProjectsSafeguardsFacade() {
		super(ProjectsSafeguards.class,Integer.class);
	}
	
	/**
	 * Busca proyecto_salvaguardas por cobeneficio
	 * @param codigoProyecto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectsSafeguards> findByProjectsCobenefits(int codigoProyecto){
		try{
			Query query=getEntityManager().createNamedQuery(Projects.CARGAR_SALVAGUARDAS_POR_COBENEFICIO);
			query.setParameter("codigoProyecto", codigoProyecto);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	/**
	 * Busca proyecto_salvaguardas por riesgo
	 * @param codigoProyecto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectsSafeguards> findByProjectsRisks(int codigoProyecto){
		try{
			Query query=getEntityManager().createNamedQuery(Projects.CARGAR_SALVAGUARDAS_POR_RIESGO);
			query.setParameter("codigoProyecto", codigoProyecto);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
}
