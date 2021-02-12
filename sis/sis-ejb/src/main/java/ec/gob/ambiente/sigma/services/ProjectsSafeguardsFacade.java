package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class ProjectsSafeguardsFacade extends AbstractFacade<ProjectsSafeguards, Integer>  {
	

	

	public ProjectsSafeguardsFacade() {
		super(ProjectsSafeguards.class,Integer.class);
	}
	
	/**
	 * Busca proyecto_salvaguardas por cobeneficio
	 * @param codigoProyecto
	 * @return
	 */
	public List<ProjectsSafeguards> buscarProyectoPorCobeneficio(int codigoProyecto) throws Exception{
		String sql="SELECT PS FROM Projects P  INNER JOIN P.projectsCobenefitsList PC INNER JOIN PC.projectsSafeguardsList PS WHERE P.projId=:codigoProyecto";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Busca proyecto_salvaguardas por riesgo
	 * @param codigoProyecto
	 * @return
	 */
	public List<ProjectsSafeguards> buscarProyectosPorRiesgo(int codigoProyecto) throws Exception{

		String sql="SELECT PS FROM Projects P  INNER JOIN P.projectsRisksList PR INNER JOIN PR.projectsSafeguardsList PS WHERE P.projId =:codigoProyecto";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
}
