package ec.gob.ambiente.sigma.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.validator.HibernateValidator;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.dao.AbstractFacade;
@Stateless
public class ProjectsFacade extends AbstractFacade<Projects, Integer> implements Serializable {


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ProjectsFacade.class);

	public ProjectsFacade() {
		super(Projects.class,Integer.class);
	}
	/**
	 * Carga todos los proyectos
	 */
	@SuppressWarnings("unchecked")
	public List<Projects> findAll(){
		try{
			Query query=getEntityManager().createNamedQuery(Projects.CARGAR_TODOS_LOS_PROYECTOS);			
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
}
