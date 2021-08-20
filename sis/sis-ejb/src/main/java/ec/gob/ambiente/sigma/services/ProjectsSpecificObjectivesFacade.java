/**
@autor proamazonia [Christian Báez]  10 ago. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.ProjectsSpecificObjectives;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class ProjectsSpecificObjectivesFacade extends AbstractFacade<ProjectsSpecificObjectives, Integer> {
	public ProjectsSpecificObjectivesFacade(){
		super(ProjectsSpecificObjectives.class,Integer.class);
	}
	/**
	 * Devuelve los objetivos del proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsSpecificObjectives> listaObjetivosProyecto(int codigoProyecto)throws Exception{
		String sql="SELECT P from ProjectsSpecificObjectives P WHERE P.psobStatus=TRUE AND P.projects.projId=:codigoProyecto ORDER BY P.psobDescription";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Devuelve los componentes del proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsSpecificObjectives> listaComponentesProyecto(int codigoProyecto)throws Exception{
		String sql="SELECT PSO FROM ProjectsSpecificObjectives PSO WHERE PSO.psobStatus = TRUE AND PSO.projects.projId=:codigoProyecto AND PSO.projects.projType IN('PRG','PROY') ORDER BY P.psobDescription";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
	
}

