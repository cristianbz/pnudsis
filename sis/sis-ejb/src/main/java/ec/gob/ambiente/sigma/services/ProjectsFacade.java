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
	/**
	 * Lista los proyectos por socio implementador
	 * @param partId
	 * @return
	 * @throws Exception
	 */
	public List<Projects> listarProyectosPorIdSocioImpl(Integer partId) throws Exception{
		String sql="SELECT P from Projects P WHERE P.projStatus=true AND P.partners.partId=:param1 ORDER BY P.projTitle";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("param1", partId);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Lista los proyectos por titulo del proyecto
	 * @param textoTitulo
	 * @return
	 * @throws Exception
	 */
	public List<Projects> listarProyectosPorTextoTitulo(String textoTitulo) throws Exception{
		String sql="SELECT P FROM Projects P WHERE P.projStatus=true AND UPPER(P.projTitle) LIKE :param1 ORDER BY P.projTitle";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("param1", "%"+textoTitulo.toUpperCase()+"%");
		return findByCreateQuery(sql, camposCondicion);
	}
}
