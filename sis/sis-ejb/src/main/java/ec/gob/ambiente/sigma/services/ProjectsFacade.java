package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class ProjectsFacade extends AbstractFacade<Projects, Integer>  {





	public ProjectsFacade() {
		super(Projects.class,Integer.class);
	}
	/**
	 * Carga todos los proyectos
	 */

	public List<Projects> buscarTodosLosProyectos() throws Exception{
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery("SELECT P FROM Projects P WHERE P.projStatus=true", camposCondicion);
	}
}
