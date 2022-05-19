/**
@autor proamazonia [Christian Báez]  10 ago. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sigma.model.ProjectsSpecificObjectives;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;

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
		String sql="SELECT PSO FROM ProjectsSpecificObjectives PSO WHERE PSO.psobStatus = TRUE AND PSO.projects.projId=:codigoProyecto ORDER BY PSO.psobDescription";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Obtiene los componentes por su codigo y por codigo de proyecto
	 * @param codigoProyecto
	 * @param codigosComponentes
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsSpecificObjectives> listaComponentesProyecto(int codigoProyecto,List<Integer> codigosComponentes)throws Exception{
		String sql="SELECT PSO FROM ProjectsSpecificObjectives PSO WHERE PSO.psobStatus = TRUE AND PSO.projects.projId=:codigoProyecto AND PSO.psobId IN(:codigosComponentes) ORDER BY P.psobDescription";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		camposCondicion.put("codigosComponentes", codigosComponentes);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Devuelve informacion de un componente
	 * @param codigo Codigo del componente a buscar
	 * @return
	 * @throws DaoException
	 */
	public ProjectsSpecificObjectives datosComponente(int codigo)throws DaoException{
		try{
			String sql="SELECT PSO FROM ProjectsSpecificObjectives PSO WHERE PSO.psobStatus = TRUE AND PSO.psobId=:codigo";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigo", codigo);			
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	
}

