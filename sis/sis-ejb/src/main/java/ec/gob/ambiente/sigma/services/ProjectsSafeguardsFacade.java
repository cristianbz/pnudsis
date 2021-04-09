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
	
	public List<Object[]> buscaProyectoPorRiesgoCobeneficio(int codigoProyecto) throws Exception {
		String sql = "select ps.prsa_id,ps.safe_id,sf.safe_order,sf.safe_level,sf.safe_code,pro.proj_id from sigma.projects pro, sigma.projects_cobenefits pc, "
					+ " sigma.projects_safeguards ps , sigma.safeguards sf"
					+ " where ps.prco_id = pc.prco_id and sf.safe_id=ps.safe_id " 
					+ " and pro.proj_id = pc.proj_id and pro.proj_id=" + codigoProyecto 
					+ " union "
					+ " select ps.prsa_id,ps.safe_id,sf.safe_order,sf.safe_level,sf.safe_code,pro.proj_id from sigma.projects pro, " 
					+ " sigma.projects_risks pr, sigma.projects_safeguards ps , sigma.safeguards sf"
					+ " where ps.prri_id = pr.prri_id and sf.safe_id=ps.safe_id " 
					+ " and pr.proj_id = pro.proj_id and pro.proj_id=" + codigoProyecto;		
		return consultaNativa(sql);
	}
}
